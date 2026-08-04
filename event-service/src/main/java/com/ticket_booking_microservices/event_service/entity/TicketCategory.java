package com.ticket_booking_microservices.event_service.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    // Quan hệ N-1: Nhiều Loại Vé có thể thuộc về 1 Sự kiện
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price; // Dùng BigDecimal cho tiền tệ để tránh sai số
    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;
    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;
}
