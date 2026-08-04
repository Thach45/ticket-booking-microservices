package com.ticket_booking_microservices.event_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VenueRequest {
    @NotBlank(message = "Tên địa điểm là bắt buộc")
    private String name;
    @NotBlank(message = "Địa chỉ là bắt buộc")
    private String address;
    @Min(value = 10, message = "Sức chứa phải lớn hơn 0")
    private Integer capacity;
}
