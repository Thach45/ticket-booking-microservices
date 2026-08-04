package com.ticket_booking_microservices.booking_service.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 1. Tên của các Exchange và Queue đích (Nơi thư đến sau 10p)
    public static final String EXCHANGE_DEAD_LETTER = "booking.dlx.exchange";
    public static final String QUEUE_TIMEOUT_PROCESS = "booking.timeout.process.queue";
    public static final String ROUTING_KEY_DLX = "booking.timeout.routing.key";
    // 2. Tên của Phòng chờ (Nơi thư bị ngâm)
    public static final String EXCHANGE_WAITING_ROOM = "booking.waiting.exchange";
    public static final String QUEUE_WAITING_ROOM = "booking.waiting.queue";
    public static final String ROUTING_KEY_WAITING = "booking.waiting.routing.key";
    // 3. Tên Routing Key cho việc gửi từ Controller vào Hòm thư
    public static final String EXCHANGE_BOOKING = "booking.direct.exchange";
    public static final String QUEUE_BOOKING = "booking.created.queue";
    public static final String ROUTING_KEY_BOOKING = "booking.routing.key";
   
    // 4. Lắng nghe từ Payment Service
    public static final String EXCHANGE_PAYMENT = "payment.direct.exchange";
    public static final String QUEUE_PAYMENT_SUCCESS = "payment.success.queue";
    public static final String ROUTING_KEY_SUCCESS = "payment.success.routing.key";
    // ==========================================
    // KHU VỰC 1: XÂY DỰNG ĐÍCH ĐẾN (SAU KHI HẾT GIỜ)
    // ==========================================
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(EXCHANGE_DEAD_LETTER);
    }

    @Bean
    public Queue timeoutProcessQueue() {
        return new Queue(QUEUE_TIMEOUT_PROCESS);
    }

    @Bean
    public Binding bindingDLX() {
        return BindingBuilder.bind(timeoutProcessQueue()).to(deadLetterExchange()).with(ROUTING_KEY_DLX);
    }

    // ==========================================
    // KHU VỰC 2: XÂY DỰNG PHÒNG CHỜ (CÓ HẸN GIỜ)
    // ==========================================
    @Bean
    public DirectExchange waitingExchange() {
        return new DirectExchange(EXCHANGE_WAITING_ROOM);
    }

    @Bean
    public Queue waitingQueue() {
        Map<String, Object> args = new HashMap<>();
        // Định nghĩa: Thư chết sẽ bị ném đi đâu? -> Ném vào DLX
        args.put("x-dead-letter-exchange", EXCHANGE_DEAD_LETTER);
        // Định nghĩa: Dán nhãn gì cho thư chết? -> Dán nhãn DLX
        args.put("x-dead-letter-routing-key", ROUTING_KEY_DLX);
        // Định nghĩa: Thư sống được bao lâu? (Tính bằng mili-giây).
        // Ở đây mình để 10.000 ms (tức là 10 giây) để test cho nhanh, thực tế bạn để 10
        // phút (600.000).
        args.put("x-message-ttl", 10000);
        return new Queue(QUEUE_WAITING_ROOM, true, false, false, args);
    }

    @Bean
    public Binding bindingWaiting() {
        return BindingBuilder.bind(waitingQueue()).to(waitingExchange()).with(ROUTING_KEY_WAITING);
    }

    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(EXCHANGE_BOOKING);
    }

    @Bean
    public Queue bookingQueue() {
        return new Queue(QUEUE_BOOKING, true);
    }

    @Bean
    public Binding bindingBooking() {
        return BindingBuilder.bind(bookingQueue())
                .to(bookingExchange())
                .with(ROUTING_KEY_BOOKING);
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    // Ép thằng RabbitTemplate phải xài bộ dịch JSON này
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(EXCHANGE_PAYMENT);
    }
    @Bean
    public Queue paymentSuccessQueue() {
        return new Queue(QUEUE_PAYMENT_SUCCESS, true);
    }
    @Bean
    public Binding bindingPaymentSuccess() {
        return BindingBuilder.bind(paymentSuccessQueue())
                .to(paymentExchange())
                .with(ROUTING_KEY_SUCCESS);
    }
}
