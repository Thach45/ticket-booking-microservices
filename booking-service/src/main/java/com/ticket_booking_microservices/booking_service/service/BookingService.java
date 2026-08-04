package com.ticket_booking_microservices.booking_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticket_booking_microservices.booking_service.config.RabbitMQConfig;
import com.ticket_booking_microservices.booking_service.dto.TicketDeductEvent;
import com.ticket_booking_microservices.booking_service.dto.request.BookingRequest;
import com.ticket_booking_microservices.booking_service.dto.response.TicketDetailResponse;
import com.ticket_booking_microservices.booking_service.entity.Booking;
import com.ticket_booking_microservices.booking_service.entity.BookingItem;
import com.ticket_booking_microservices.booking_service.feign.EventServiceClient;
import com.ticket_booking_microservices.booking_service.repository.BookingRepository;

import feign.FeignException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
        private final BookingRepository bookingRepository;
        private final RabbitTemplate rabbitTemplate;
        private final EventServiceClient eventServiceClient;

        @Transactional // Đảm bảo việc lưu DB thành công mới chạy tiếp
        public Booking createBooking(BookingRequest request) {
                // 0. GỌI ĐIỆN SANG EVENT SERVICE ĐỂ HỎI GIÁ VÉ THẬT!
                BigDecimal unitPrice;
                try {
                        TicketDetailResponse ticketDetail = eventServiceClient.getTicketDetail(request.getTicketCategoryId());
                        if (!ticketDetail.getEventId().equals(request.getEventId())) {
                                throw new RuntimeException("Lỗi: Cái vé này không thuộc về sự kiện mà bạn chọn!");
                        }
                        unitPrice = ticketDetail.getPrice();
                } catch (FeignException.NotFound e) {
                        throw new RuntimeException("Failed to get ticket price from event service", e);
                }

                // Tính tổng tiền = Giá * Số lượng
                BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));
                // 1. Tạo Entity hóa đơn (Booking)
                Booking newBooking = Booking.builder()
                                .userId(request.getUserId())
                                .eventId(request.getEventId())
                                .status("PENDING") // Hóa đơn mới tạo luôn ở trạng thái chờ thanh toán
                               
                                .totalAmount(totalAmount)
                                .build();
                BookingItem item = BookingItem.builder()
                .booking(newBooking)
                .ticketCategoryId(request.getTicketCategoryId())
                .quantity(request.getQuantity())
                .build();
                newBooking.setItems(java.util.Collections.singletonList(item));
                // 2. Lưu xuống Database (Lúc này JPA sẽ tự tạo ra cái UUID thật)
                Booking savedBooking = bookingRepository.save(newBooking);
                // 3. Chuẩn bị Sự kiện (Event) để báo cho Event Service biết đường trừ vé
                TicketDeductEvent event = TicketDeductEvent.builder()
                                .bookingId(savedBooking.getId()) // Lấy ID thật vừa sinh ra
                                .ticketCategoryId(request.getTicketCategoryId())
                                .quantityToDeduct(request.getQuantity())
                                .build();
                // 4. Bắn sự kiện trừ vé vào RabbitMQ
                rabbitTemplate.convertAndSend(
                                RabbitMQConfig.EXCHANGE_BOOKING,
                                RabbitMQConfig.ROUTING_KEY_BOOKING,
                                event);

                // 5. Bắn thêm 1 sự kiện nữa vào Phòng Chờ (Timeout 10 phút) để nhắc việc thanh
                // toán
                // (Đây là cái DLX bạn đã làm ở bài trước, mình ghép chung vào đây luôn cho hoàn
                // chỉnh)
                String timeoutMessage = savedBooking.getId().toString();
                rabbitTemplate.convertAndSend(
                                RabbitMQConfig.EXCHANGE_WAITING_ROOM,
                                RabbitMQConfig.ROUTING_KEY_WAITING,
                                timeoutMessage);
                System.out.println("Đã tạo Booking thành công và bắn 2 sự kiện vào RabbitMQ!");

                return savedBooking;
        }

        public List<Booking> getAllBookings() {
                return bookingRepository.findAll();
        }
}
