package com.ticket_booking_microservices.event_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticket_booking_microservices.event_service.entity.TicketCategory;

public interface TicketCategoryRepository extends JpaRepository<TicketCategory, UUID> {
}
