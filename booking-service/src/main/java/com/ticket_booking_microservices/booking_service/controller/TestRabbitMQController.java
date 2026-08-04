package com.ticket_booking_microservices.booking_service.controller;

import com.ticket_booking_microservices.booking_service.config.RabbitMQConfig;
import com.ticket_booking_microservices.booking_service.dto.TicketDeductEvent;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class TestRabbitMQController {
    @Autowired
    private RabbitTemplate rabbitTemplate; // Công cụ đắc lực của Spring để gửi tin
    @GetMapping("/test-send")
    public String createBooking(UUID ticketId, int amount) {
        UUID mockBookingId = UUID.randomUUID(); // Giả sử đã lưu hóa đơn
        // Tạo cục dữ liệu chuẩn bị gửi đi
        TicketDeductEvent event = TicketDeductEvent.builder()
                .ticketCategoryId(ticketId)
                .quantityToDeduct(amount) // Ví dụ khách mua 2 vé
                .bookingId(mockBookingId)
                .build();
        // RabbitMQ sẽ tự động biến cục Object event này thành chuỗi JSON và bắn đi!
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_BOOKING,
                RabbitMQConfig.ROUTING_KEY_BOOKING,
                event
        );
        return "Đã bắn lệnh yêu cầu trừ vé!";
    }
}