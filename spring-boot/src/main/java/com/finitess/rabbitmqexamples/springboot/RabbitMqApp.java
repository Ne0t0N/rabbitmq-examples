package com.finitess.rabbitmqexamples.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitMqApp {

    public static final String DIRECT_EXCHANGE_NAME = "exchange.direct";
    public static final String DIRECT_QUEUE_NAME = "queue.direct";
    public static final String DEFAULT_ROUTING_KEY = "default";

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqApp.class, args);
    }
}
