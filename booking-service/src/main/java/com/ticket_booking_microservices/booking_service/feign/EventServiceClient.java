package com.ticket_booking_microservices.booking_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ticket_booking_microservices.booking_service.dto.response.TicketDetailResponse;

import java.math.BigDecimal;
import java.util.UUID;
// Khai báo: tạo cho tao 1 cái ống nối sang cổng 8082 của Event Service
@FeignClient(name = "event-service", url = "http://localhost:8082", fallback = EventServiceFallback.class)
public interface EventServiceClient {
    // Khai báo hàm: Cứ gọi hàm này thì mày tự động bắn 1 request GET sang cái link kia nhé
    @GetMapping("/api/tickets/{ticketId}/detail")
    TicketDetailResponse getTicketDetail(@PathVariable("ticketId") UUID ticketId);
}

