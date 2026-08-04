package com.ticket_booking_microservices.event_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.ticket_booking_microservices.event_service.dto.request.EventRequest;
import com.ticket_booking_microservices.event_service.entity.Event;
import com.ticket_booking_microservices.event_service.entity.Venue;
import com.ticket_booking_microservices.event_service.repository.EventRepository;
import com.ticket_booking_microservices.event_service.repository.VenueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public Event createEvent(EventRequest request) {
        // 1. Lấy thông tin Venue từ ID
        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + request.getVenueId()));

        // 2. Tạo Entity Event mới
        Event event = Event.builder()
                .name(request.getName())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(request.getStatus())
                .venue(venue) // Lưu địa điểm vào sự kiện
                .build();

        // 3. Lưu vào DB
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    public Event updateEvent(UUID id, EventRequest request) {
        Event event = getEventById(id);
        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + request.getVenueId()));

        event.setVenue(venue);
        event.setName(request.getName());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setStatus(request.getStatus());
        return eventRepository.save(event);
    }

    public void deleteEvent(UUID id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }
}
