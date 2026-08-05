package com.ticket_booking_microservices.payment_service.dto.request;

import java.util.UUID;

import lombok.Data;

@Data
public class PaymentRequest {
    private UUID bookingId;
    private String provider; // "VNPAY" hoặc "STRIPE"
    
    // Thêm trường này: Cái "Căn cước" duy nhất cho mỗi nỗ lực thanh toán
    private String idempotencyKey; 
}