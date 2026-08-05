package com.ticket_booking_microservices.ticketing_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_id", nullable = false)
    private UUID bookingId; // Vé này thuộc Hóa đơn nào?

    @Column(name = "qr_code_url")
    private String qrCodeUrl; // Link chứa ảnh QR Code để quét lúc check-in

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status; // VALID (Hợp lệ), USED (Đã quét), EXPIRED (Hết hạn)

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum TicketStatus {
        VALID, USED, EXPIRED
    }
}