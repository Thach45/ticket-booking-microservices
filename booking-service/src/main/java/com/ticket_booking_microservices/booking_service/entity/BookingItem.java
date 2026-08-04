package com.ticket_booking_microservices.booking_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
@Entity
@Table(name = "booking_items")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BookingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    // Nhiều Món hàng thuộc về 1 Hóa đơn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Booking booking;
    @Column(name = "ticket_category_id", nullable = false)
    private UUID ticketCategoryId;
    @Column(nullable = false)
    private Integer quantity;
}