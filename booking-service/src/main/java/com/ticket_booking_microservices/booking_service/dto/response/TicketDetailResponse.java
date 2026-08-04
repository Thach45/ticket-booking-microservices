package com.ticket_booking_microservices.booking_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDetailResponse {
    private UUID id;
    private BigDecimal price;
    private UUID eventId;
}
