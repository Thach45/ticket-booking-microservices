package com.ticket_booking_microservices.booking_service.dto.request;

import lombok.Data;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

@Data
public class BookingRequest {
    @NotBlank(message = "User ID is required")
    private UUID userId; // Ai mua?
    @NotBlank(message = "Event ID is required")
    private UUID eventId;           // Mua sự kiện nào?
    @NotBlank(message = "Ticket Category ID is required")
    private UUID ticketCategoryId;  // Mua loại vé nào (VIP hay Thường)?
    @NotBlank(message = "Quantity is required")
    private int quantity;           // Mua bao nhiêu cái?
}
