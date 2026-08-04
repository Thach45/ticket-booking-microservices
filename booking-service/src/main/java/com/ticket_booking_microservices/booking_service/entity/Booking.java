package com.ticket_booking_microservices.booking_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "bookings")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    // Chỉ lưu ID của User (Auth Service) chứ không dùng @ManyToOne
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    // Chỉ lưu ID của Event (Event Service)
    @Column(name = "event_id", nullable = false)
    private UUID eventId;
    // Trạng thái: PENDING, PAID, CANCELLED
    @Column(nullable = false)
    private String status;
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    // Tự động lấy giờ hệ thống lúc tạo mới hóa đơn
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    // Nối với bảng BookingItem
    // CascadeType.ALL có nghĩa là: Hễ lưu cái Hóa đơn, thì tự động lưu mấy cái Món hàng bên trong luôn (đỡ phải gọi repository 2 lần)
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<BookingItem> items;
}
