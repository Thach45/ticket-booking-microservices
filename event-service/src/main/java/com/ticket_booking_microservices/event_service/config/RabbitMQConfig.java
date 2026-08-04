package com.ticket_booking_microservices.event_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_UNLOCK = "booking.unlock.queue";
    public static final String QUEUE_BOOKING = "booking.created.queue";
    public static final String EXCHANGE_BOOKING = "booking.direct.exchange";
    public static final String ROUTING_KEY_UNLOCK = "booking.unlock.routing.key";

    @Bean
    public Queue unlockQueue() {
        return new Queue(QUEUE_UNLOCK, true);
    }

    @Bean
    public Queue bookingQueue() {
        return new Queue(QUEUE_BOOKING, true);
    }

    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(EXCHANGE_BOOKING);
    }

    @Bean
    public Binding bindingUnlock() {
        return BindingBuilder.bind(unlockQueue()).to(bookingExchange()).with(ROUTING_KEY_UNLOCK);
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
}
