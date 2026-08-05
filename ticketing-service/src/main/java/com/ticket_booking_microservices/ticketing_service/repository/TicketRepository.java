package com.ticket_booking_microservices.ticketing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ticket_booking_microservices.ticketing_service.entity.Ticket;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

}
