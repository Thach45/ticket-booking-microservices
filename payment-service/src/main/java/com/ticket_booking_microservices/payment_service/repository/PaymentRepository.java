package com.ticket_booking_microservices.payment_service.repository;

import com.ticket_booking_microservices.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    // Công cụ để lục tìm hóa đơn cũ dựa trên key
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
}