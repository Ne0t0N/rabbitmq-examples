package com.finitess.rabbitmqexamples.springboot;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.finitess.rabbitmqexamples.springboot.RabbitMqApp.DEFAULT_ROUTING_KEY;
import static com.finitess.rabbitmqexamples.springboot.RabbitMqApp.DIRECT_EXCHANGE_NAME;

@Component
public class RabbitMqMessageProducer {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public RabbitMqMessageProducer(final AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void produce(final String message) {
        final Message toSend = MessageBuilder.withBody(message.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .build();
        amqpTemplate.send(DIRECT_EXCHANGE_NAME, DEFAULT_ROUTING_KEY, toSend);
    }
}
