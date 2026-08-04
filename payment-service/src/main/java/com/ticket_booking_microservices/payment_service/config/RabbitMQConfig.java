package com.ticket_booking_microservices.payment_service.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Trạm trung chuyển để Payment phát loa thông báo
    public static final String EXCHANGE_PAYMENT = "payment.direct.exchange";
    public static final String ROUTING_KEY_SUCCESS = "payment.success.routing.key";

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(EXCHANGE_PAYMENT);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Ép RabbitTemplate dịch dữ liệu sang JSON trước khi gửi
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}