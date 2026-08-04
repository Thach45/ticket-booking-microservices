package com.ticket_booking_microservices.payment_service.controller;

import com.ticket_booking_microservices.payment_service.dto.request.PaymentRequest;
import com.ticket_booking_microservices.payment_service.entity.Payment;
import com.ticket_booking_microservices.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> pay(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }
}