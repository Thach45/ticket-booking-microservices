package com.ticket_booking_microservices.ticketing_service.service;

import com.ticket_booking_microservices.ticketing_service.config.RabbitMQConfig;
import com.ticket_booking_microservices.ticketing_service.dto.event.PaymentSuccessEvent;
import com.ticket_booking_microservices.ticketing_service.entity.Ticket;
import com.ticket_booking_microservices.ticketing_service.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketConsumer {

    private final TicketRepository ticketRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_TICKETING_PAYMENT_SUCCESS)
    public void generateTicket(PaymentSuccessEvent event) {
        System.out.println("🖨️ Ticketing Service nhận thông báo thanh toán. Đang tiến hành in vé cho Hóa đơn: " + event.getBookingId());

        // Thực tế: Sinh ra chuỗi String mã hóa rồi gọi API Google tạo ảnh QR Code
        String mockQrCodeUrl = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" + event.getBookingId();

        Ticket ticket = Ticket.builder()
                .bookingId(event.getBookingId())
                .qrCodeUrl(mockQrCodeUrl)
                .status(Ticket.TicketStatus.VALID)
                .build();

        ticketRepository.save(ticket);
        
        System.out.println("🎟️ IN VÉ THÀNH CÔNG! Link QR Code để gửi qua Email: " + mockQrCodeUrl);
        // Chỗ này bạn có thể cắm thêm thư viện Gửi Email để gửi cho khách nhé.
    }
}