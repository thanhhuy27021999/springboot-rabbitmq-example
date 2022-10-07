package com.javatechie.rabbitmq.demo.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.rabbitmq.demo.config.MessagingConfig;
import com.javatechie.rabbitmq.demo.dto.Order;
import com.javatechie.rabbitmq.demo.dto.OrderStatus;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderPublisher {

    @Autowired
    private RabbitTemplate template;
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/{restaurantName}")
    public String bookOrder(@RequestBody Order order, @PathVariable String restaurantName) throws JsonProcessingException {
        order.setOrderId(UUID.randomUUID().toString());
        //restaurantservice
        //payment service
        OrderStatus orderStatus = new OrderStatus(order, "PROCESS", "order placed succesfully in " + restaurantName);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("x-delay", 5000);
        //template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, orderStatus);
        //template.convertAndSend("DELAY_TEST_EXCHANGE", "DELAY_TEST_ROUTING", orderStatus);
        System.out.println("Start sending " + System.currentTimeMillis());
        template.send("DELAY_TEST_EXCHANGE","DELAY_TEST_ROUTING", MessageBuilder.withBody(objectMapper.writeValueAsString(orderStatus).getBytes(StandardCharsets.UTF_8)).andProperties(messageProperties).build());
        return "Success !!";
    }
}
