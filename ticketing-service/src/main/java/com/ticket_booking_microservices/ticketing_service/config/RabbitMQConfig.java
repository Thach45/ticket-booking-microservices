package com.ticket_booking_microservices.ticketing_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Vẫn chỉ vào đúng cái Đài phát thanh (Exchange) của Payment
    public static final String EXCHANGE_PAYMENT = "payment.direct.exchange";
    
    // TÊN RADIO PHẢI KHÁC BIỆT: Tự tạo 1 hàng đợi riêng cho Ticketing
    public static final String QUEUE_TICKETING_PAYMENT_SUCCESS = "ticketing.payment.success.queue";
    
    // Vẫn dò đúng cái tần số (Routing key) đó
    public static final String ROUTING_KEY_SUCCESS = "payment.success.routing.key";

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(EXCHANGE_PAYMENT);
    }

    @Bean
    public Queue ticketingPaymentSuccessQueue() {
        return new Queue(QUEUE_TICKETING_PAYMENT_SUCCESS, true);
    }

    @Bean
    public Binding bindingPaymentSuccess() {
        return BindingBuilder.bind(ticketingPaymentSuccessQueue())
                .to(paymentExchange())
                .with(ROUTING_KEY_SUCCESS);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}