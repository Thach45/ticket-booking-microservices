package com.ticket_booking_microservices.event_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ticket_booking_microservices.event_service.dto.request.VenueRequest;
import com.ticket_booking_microservices.event_service.entity.Venue;
import com.ticket_booking_microservices.event_service.repository.VenueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository venueRepository;
    
    public Venue createVenue(VenueRequest request) {
        Venue venue = Venue.builder()
                .name(request.getName())
                .address(request.getAddress())
                .capacity(request.getCapacity())
                .build();
        return venueRepository.save(venue);
    }

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Venue getVenueById(UUID id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + id));
    }

    public Venue updateVenue(UUID id, VenueRequest request) {
        Venue venue = getVenueById(id);
        venue.setName(request.getName());
        venue.setAddress(request.getAddress());
        venue.setCapacity(request.getCapacity());
        return venueRepository.save(venue);
    }

    public void deleteVenue(UUID id) {
        if (!venueRepository.existsById(id)) {
            throw new RuntimeException("Venue not found with id: " + id);
        }
        venueRepository.deleteById(id);
    }
}
