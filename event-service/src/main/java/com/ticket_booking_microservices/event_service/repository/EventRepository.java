package com.ticket_booking_microservices.event_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticket_booking_microservices.event_service.entity.Event;

public interface EventRepository extends JpaRepository<Event, UUID> {

}
