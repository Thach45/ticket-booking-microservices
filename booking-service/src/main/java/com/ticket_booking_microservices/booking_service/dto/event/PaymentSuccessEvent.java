package com.ticket_booking_microservices.booking_service.dto.event;

import lombok.Data;
import java.util.UUID;
@Data
public class PaymentSuccessEvent {
    private UUID bookingId;
    private String transactionId;
}