package com.ticket_booking_microservices.payment_service.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ticket_booking_microservices.payment_service.dto.response.BookingResponse;

import java.util.UUID;
// Gọi thẳng sang cổng 8083 của Booking (Sau này có Eureka Service Discovery thì xóa cái url đi)
@FeignClient(name = "booking-service", url = "http://localhost:8083")
public interface BookingClient {
    // Bê y chang cái signature hàm GET của BookingController qua đây (Nên tạo 1 class BookingDto riêng để hứng data nhé)
    @GetMapping("/api/bookings/{id}")
    BookingResponse getBookingById(@PathVariable("id") UUID id); 
}