package com.ticket_booking_microservices.event_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ticket_booking_microservices.event_service.dto.request.TicketCategoryRequest;
import com.ticket_booking_microservices.event_service.dto.response.TicketDetailResponse;
import com.ticket_booking_microservices.event_service.entity.Event;
import com.ticket_booking_microservices.event_service.entity.TicketCategory;
import com.ticket_booking_microservices.event_service.repository.EventRepository;
import com.ticket_booking_microservices.event_service.repository.TicketCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketCategoryService {
    private final TicketCategoryRepository ticketCategoryRepository;
    private final EventRepository eventRepository;

    public TicketCategory createTicketCategory(TicketCategoryRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + request.getEventId()));

        TicketCategory ticketCategory = TicketCategory.builder()
                .event(event)
                .name(request.getName())
                .price(request.getPrice())
                .totalQuantity(request.getTotalQuantity())
                .availableQuantity(request.getAvailableQuantity())
                .build();
        return ticketCategoryRepository.save(ticketCategory);
    }

    public List<TicketCategory> getAllTicketCategories() {
        return ticketCategoryRepository.findAll();
    }

    public TicketCategory getTicketCategoryById(UUID id) {
        return ticketCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TicketCategory not found with id: " + id));
    }

    public TicketCategory updateTicketCategory(UUID id, TicketCategoryRequest request) {
        TicketCategory ticketCategory = getTicketCategoryById(id);
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + request.getEventId()));

        ticketCategory.setEvent(event);
        ticketCategory.setName(request.getName());
        ticketCategory.setPrice(request.getPrice());
        ticketCategory.setTotalQuantity(request.getTotalQuantity());
        ticketCategory.setAvailableQuantity(request.getAvailableQuantity());
        return ticketCategoryRepository.save(ticketCategory);
    }

    public void deleteTicketCategory(UUID id) {
        if (!ticketCategoryRepository.existsById(id)) {
            throw new RuntimeException("TicketCategory not found with id: " + id);
        }
        ticketCategoryRepository.deleteById(id);
    }

    public TicketDetailResponse getTicketDetail(UUID ticketId) {
        TicketCategory ticketCategory = ticketCategoryRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("TicketCategory not found with id: " + ticketId));
        return TicketDetailResponse.builder()
                .id(ticketCategory.getId())
                .price(ticketCategory.getPrice())
                .eventId(ticketCategory.getEvent().getId())
                .build();
    }

    
}
