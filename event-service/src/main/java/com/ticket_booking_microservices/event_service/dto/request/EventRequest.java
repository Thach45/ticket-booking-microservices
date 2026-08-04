package com.ticket_booking_microservices.event_service.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ticket_booking_microservices.event_service.enums.EventStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class EventRequest {
    @NotNull(message = "Venue ID là bắt buộc")
    private UUID venueId;
    @NotBlank(message = "Tên sự kiện là bắt buộc")
    private String name;
    @NotNull(message = "Thời gian bắt đầu là bắt buộc")
    private LocalDateTime startTime;
    @NotNull(message = "Thời gian kết thúc là bắt buộc")
    private LocalDateTime endTime;
    @NotNull(message = "Trạng thái là bắt buộc")
    private EventStatus status;
}
