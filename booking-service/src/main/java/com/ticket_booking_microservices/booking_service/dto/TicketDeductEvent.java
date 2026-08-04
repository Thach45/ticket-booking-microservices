package com.ticket_booking_microservices.booking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDeductEvent {
    private UUID ticketCategoryId; // Mã loại vé cần trừ
    private Integer quantityToDeduct; // Số lượng cần trừ
    private UUID bookingId; // Gửi kèm mã hóa đơn để sau này dễ log/trace
}