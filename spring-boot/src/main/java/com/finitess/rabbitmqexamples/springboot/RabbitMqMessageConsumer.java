package com.finitess.rabbitmqexamples.springboot;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.finitess.rabbitmqexamples.springboot.RabbitMqApp.DEFAULT_ROUTING_KEY;
import static com.finitess.rabbitmqexamples.springboot.RabbitMqApp.DIRECT_EXCHANGE_NAME;
import static com.finitess.rabbitmqexamples.springboot.RabbitMqApp.DIRECT_QUEUE_NAME;

@Component
public class RabbitMqMessageConsumer {

    private final RabbitMqMessageCallback messageCallback;

    @Autowired
    public RabbitMqMessageConsumer(final RabbitMqMessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

    @RabbitListener(autoStartup = "true", ackMode = "AUTO", bindings = @QueueBinding(
            value = @Queue(value = DIRECT_QUEUE_NAME),
            exchange = @Exchange(value = DIRECT_EXCHANGE_NAME),
            key = DEFAULT_ROUTING_KEY)
    )
    public void receive(final String message) {
        messageCallback.accept(message);
    }
}
