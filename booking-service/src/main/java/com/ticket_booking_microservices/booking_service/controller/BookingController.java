package com.ticket_booking_microservices.booking_service.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ticket_booking_microservices.booking_service.dto.request.BookingRequest;
import com.ticket_booking_microservices.booking_service.entity.Booking;
import com.ticket_booking_microservices.booking_service.service.BookingService;
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    // Thay đổi thành POST và dùng @RequestBody để lấy JSON từ client
    @PostMapping
    public ResponseEntity<Booking> placeOrder(@RequestBody BookingRequest request) {
        Booking booking = bookingService.createBooking(request);
        return ResponseEntity.ok(booking);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }
}