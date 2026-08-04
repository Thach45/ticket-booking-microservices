package com.ticket_booking_microservices.booking_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticket_booking_microservices.booking_service.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    
}
