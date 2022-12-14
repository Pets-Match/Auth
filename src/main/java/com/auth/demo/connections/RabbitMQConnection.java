package com.auth.demo.connections;

import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class RabbitMQConnection {

    @Value("${crud.rabbitmq.exchange}")
    String exchange;

    @Value("routingKeyCaso")
    String routingKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void casoProducerMensagem(JSONObject json) {
        System.out.println(exchange);
        System.out.println(routingKey);
        rabbitTemplate.convertAndSend(exchange, routingKey, json);
    }
}
