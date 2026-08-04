package com.ticket_booking_microservices.event_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.ticket_booking_microservices.event_service.config.RabbitMQConfig;
import com.ticket_booking_microservices.event_service.dto.request.TicketDeductEvent;
import com.ticket_booking_microservices.event_service.dto.request.TicketUnlockEvent;
import com.ticket_booking_microservices.event_service.entity.TicketCategory;
import com.ticket_booking_microservices.event_service.repository.TicketCategoryRepository;

@Service
@RequiredArgsConstructor
public class EventConsumer {
    // Gọi Repository để chọc xuống DB của Event Service
    private final TicketCategoryRepository ticketCategoryRepository;
    
    @RabbitListener(queues = RabbitMQConfig.QUEUE_UNLOCK)
    public void handleTicketUnlock(TicketUnlockEvent event) {
        System.out.println("♻️ ===> Bắt được yêu cầu HOÀN VÉ cho Hóa đơn: " + event.getBookingId());
        // 1. Tìm vé trong DB
        TicketCategory ticket = ticketCategoryRepository.findById(event.getTicketCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại vé này!"));
        // 2. CỘNG LẠI SỐ LƯỢNG VÉ ĐÃ BỊ HỦY
        int newQuantity = ticket.getAvailableQuantity() + event.getQuantityToReturn();
        ticket.setAvailableQuantity(newQuantity);
        // 3. Lưu xuống Database
        ticketCategoryRepository.save(ticket);
        
        System.out.println("♻️ ===> Đã hoàn vé thành công. Số vé trong kho hiện tại: " + newQuantity);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_BOOKING)
    public void handleTicketDeduct(TicketDeductEvent event) {
        System.out.println("🔥 ===> Bắt được yêu cầu TRỪ VÉ cho Hóa đơn: " + event.getBookingId());
        
        TicketCategory ticket = ticketCategoryRepository.findById(event.getTicketCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại vé này!"));
        
        int newQuantity = ticket.getAvailableQuantity() - event.getQuantityToDeduct();
        if (newQuantity < 0) {
            throw new RuntimeException("Không đủ vé!");
        }
        
        ticket.setAvailableQuantity(newQuantity);
        ticketCategoryRepository.save(ticket);
        
        System.out.println("🔥 ===> Đã trừ vé thành công. Số vé trong kho hiện tại: " + newQuantity);
    }
}