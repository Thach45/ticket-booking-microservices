package com.ticket_booking_microservices.booking_service.feign;

import com.ticket_booking_microservices.booking_service.dto.response.TicketDetailResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventServiceFallback implements EventServiceClient {
    @Override
    public TicketDetailResponse getTicketDetail(UUID ticketCategoryId) {
        // Đây là hàm sẽ chạy khi event-service bị sập hoặc quá tải
        // Trả về một Exception custom hoặc một response báo lỗi
        throw new RuntimeException("Hệ thống vé đang quá tải hoặc bảo trì, vui lòng quay lại sau!");
    }
}