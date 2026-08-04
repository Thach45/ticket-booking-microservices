package com.ticket_booking_microservices.booking_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticket_booking_microservices.booking_service.config.RabbitMQConfig;
import com.ticket_booking_microservices.booking_service.dto.event.PaymentSuccessEvent;
import com.ticket_booking_microservices.booking_service.dto.request.TicketUnlockEvent;
import com.ticket_booking_microservices.booking_service.entity.Booking;
import com.ticket_booking_microservices.booking_service.entity.BookingItem;
import com.ticket_booking_microservices.booking_service.repository.BookingRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingConsumer {
    private final BookingRepository bookingRepository;
    private final RabbitTemplate rabbitTemplate;

    // Lắng nghe Queue của DLX (Hết giờ)
    @RabbitListener(queues = RabbitMQConfig.QUEUE_TIMEOUT_PROCESS)
    @Transactional
    public void receiveTimeoutMessage(String bookingIdStr) {
        System.out.println("🔥 [DLX] Hết giờ 10s cho Hóa đơn: " + bookingIdStr);

        UUID bookingId = UUID.fromString(bookingIdStr);

        // 1. Kiểm tra Database xem khách đã trả tiền chưa
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null)
            return;
        if ("PENDING".equals(booking.getStatus())) {
            // 2. KHÁCH BÙNG! -> Cập nhật trạng thái thành CANCELLED
            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);
            System.out.println("❌ Hóa đơn " + bookingId + " đã bị HỦY vì quá hạn thanh toán!");
            // 3. SAGA PATTERN: Gửi lệnh đền bù (Hoàn vé) sang Event Service
            for (BookingItem item : booking.getItems()) {

                TicketUnlockEvent unlockEvent = TicketUnlockEvent.builder()
                        .bookingId(booking.getId())
                        .ticketCategoryId(item.getTicketCategoryId()) // LẤY ID VÉ THẬT TỪ DB
                        .quantityToReturn(item.getQuantity()) // LẤY SỐ LƯỢNG THẬT TỪ DB!
                        .build();
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EXCHANGE_BOOKING,
                        "booking.unlock.routing.key",
                        unlockEvent);
            }
        } else {
            System.out.println("✅ Hóa đơn này đã được thanh toán (PAID). Bỏ qua!");
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PAYMENT_SUCCESS)
    @Transactional
    public void receivePaymentSuccess(
            PaymentSuccessEvent event) {
        System.out.println("💰 Nhận được tin báo ĐÃ THANH TOÁN cho Hóa đơn: " + event.getBookingId());

        Booking booking = bookingRepository.findById(event.getBookingId()).orElse(null);
        if (booking == null) {
            System.out.println("Lỗi: Không tìm thấy hóa đơn!");
            return;
        }

        if ("PENDING".equals(booking.getStatus())) {
            booking.setStatus("PAID");
            bookingRepository.save(booking);
            System.out.println("✅ Đã chốt đơn (PAID). Hóa đơn này an toàn, 10s nữa có bị DLX check cũng không sợ!");
        } else {
            System.out.println("⚠️ Hóa đơn này đã ở trạng thái: " + booking.getStatus() + ", không thể cập nhật PAID.");
        }
    }
}