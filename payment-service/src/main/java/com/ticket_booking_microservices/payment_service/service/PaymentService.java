package com.ticket_booking_microservices.payment_service.service;

import com.ticket_booking_microservices.payment_service.config.RabbitMQConfig;
import com.ticket_booking_microservices.payment_service.dto.event.PaymentSuccessEvent;
import com.ticket_booking_microservices.payment_service.dto.request.PaymentRequest;
import com.ticket_booking_microservices.payment_service.entity.Payment;
import com.ticket_booking_microservices.payment_service.enums.PaymentStatus;
import com.ticket_booking_microservices.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;

    public Payment processPayment(PaymentRequest request) {
        
        // GIẢ LẬP: Khách hàng luôn thanh toán thành công 9.000.000 VND qua VNPay
        Payment payment = Payment.builder()
                .bookingId(request.getBookingId())
                .amount(new BigDecimal("9000000")) // Tạm hardcode, thực tế phải lấy từ Booking
                .currency("VND")
                .provider(request.getProvider())
                .status(PaymentStatus.SUCCESS)
                .transactionId("VNP" + System.currentTimeMillis())
                .build();

        // 1. Lưu vào Database của Payment
        Payment savedPayment = paymentRepository.save(payment);

        // 2. Gói tin báo hỉ (Event)
        PaymentSuccessEvent event = PaymentSuccessEvent.builder()
                .bookingId(savedPayment.getBookingId())
                .transactionId(savedPayment.getTransactionId())
                .build();

        // 3. Đem ra RabbitMQ phát loa thông báo
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_PAYMENT,
                RabbitMQConfig.ROUTING_KEY_SUCCESS,
                event
        );

        System.out.println("✅ Đã chốt đơn thanh toán thành công cho Hóa đơn: " + request.getBookingId());
        return savedPayment;
    }
}