package com.ticket_booking_microservices.event_service.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticket_booking_microservices.event_service.dto.request.TicketCategoryRequest;
import com.ticket_booking_microservices.event_service.dto.response.TicketDetailResponse;
import com.ticket_booking_microservices.event_service.entity.TicketCategory;
import com.ticket_booking_microservices.event_service.service.TicketCategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketCategoryService ticketCategoryService;

    @PostMapping
    public ResponseEntity<TicketCategory> createTicketCategory(@RequestBody @Valid TicketCategoryRequest request) {
        return new ResponseEntity<>(ticketCategoryService.createTicketCategory(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TicketCategory>> getAllTicketCategories() {
        return ResponseEntity.ok(ticketCategoryService.getAllTicketCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketCategory> getTicketCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(ticketCategoryService.getTicketCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketCategory> updateTicketCategory(@PathVariable UUID id, @RequestBody @Valid TicketCategoryRequest request) {
        return ResponseEntity.ok(ticketCategoryService.updateTicketCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketCategory(@PathVariable UUID id) {
        ticketCategoryService.deleteTicketCategory(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{ticketId}/detail")
    public ResponseEntity<TicketDetailResponse> getTicketPrice(@PathVariable UUID ticketId) {
        return ResponseEntity.ok(ticketCategoryService.getTicketDetail(ticketId));
    }
}
