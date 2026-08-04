package com.ticket_booking_microservices.event_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TicketUnlockEvent {
    private UUID ticketCategoryId;
    private Integer quantityToReturn;
    private UUID bookingId; // Gửi kèm để Event Service biết trả vé cho đơn nào
}
