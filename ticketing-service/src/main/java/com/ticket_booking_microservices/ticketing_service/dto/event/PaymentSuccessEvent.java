package com.ticket_booking_microservices.ticketing_service.dto.event;

import lombok.Data;
import java.util.UUID;
@Data
public class PaymentSuccessEvent {
    private UUID bookingId;
    private String transactionId;
}