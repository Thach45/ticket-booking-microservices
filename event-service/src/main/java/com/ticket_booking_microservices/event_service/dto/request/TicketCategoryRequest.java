package com.ticket_booking_microservices.event_service.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketCategoryRequest {
    @NotNull(message = "Event ID là bắt buộc")
    private UUID eventId;

    @NotBlank(message = "Tên loại vé là bắt buộc")
    private String name;

    @NotNull(message = "Giá là bắt buộc")
    private BigDecimal price;

    @NotNull(message = "Tổng số lượng là bắt buộc")
    @Min(value = 1, message = "Tổng số lượng phải lớn hơn 0")
    private Integer totalQuantity;

    @NotNull(message = "Số lượng còn lại là bắt buộc")
    @Min(value = 0, message = "Số lượng còn lại không được âm")
    private Integer availableQuantity;
}
