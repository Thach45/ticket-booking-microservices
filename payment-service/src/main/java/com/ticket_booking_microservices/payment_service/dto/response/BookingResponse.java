package com.ticket_booking_microservices.payment_service.dto.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BookingResponse {
    private String id;
    private BigDecimal totalAmount;
    private String status;
}
