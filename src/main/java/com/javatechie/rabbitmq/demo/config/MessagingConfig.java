package com.javatechie.rabbitmq.demo.config;

import lombok.SneakyThrows;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
public class MessagingConfig {

    public static final String QUEUE = "javatechie_queue";
    public static final String EXCHANGE = "javatechie_exchange";
    public static final String ROUTING_KEY = "javatechie_routingKey";

    @Bean
    public Queue queue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange","");
        args.put("x-dead-letter-routing-key","test");
        args.put("x-message-ttl", 7000);
        Queue queue = new Queue("testDelayTemp", true, false, false,args );
        return queue;
    }

    @Bean
    public Queue desQuee() {
        return new Queue("test");
    }
    @Bean
    public CustomExchange exchange() {
//        Map<String, Object> args = new HashMap<>();
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange("DELAY_TEST_EXCHANGE", "x-delayed-message", true, false, args);
        return new CustomExchange("DELAY_TEST_EXCHANGE", "direct");
    }

    @Bean
    public Binding binding(Queue queue, CustomExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("DELAY_TEST_ROUTING").noargs();
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
