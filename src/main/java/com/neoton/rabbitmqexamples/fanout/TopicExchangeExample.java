package com.neoton.rabbitmqexamples.fanout;

import com.neoton.rabbitmqexamples.common.PreconditionUtils;
import com.neoton.rabbitmqexamples.common.RabbitMqUtils;
import com.neoton.rabbitmqexamples.common.SingleDeliveryConsumer;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TopicExchangeExample implements ExchangeExample {

    private static final Logger LOG = LoggerFactory.getLogger(TopicExchangeExample.class);
    private static final String EXCHANGE = "sample.topic";
    private static final String BOUND_QUEUE = "sample.topic.bound.queue";
    private static final String ROUTING_KEY = "sample.direct.routing.key";
    private static final String ROUTING_KEY_PART = "sample.direct.#";

    @Override
    public void produce(String message) {
        Channel channel = RabbitMqUtils.createChannel();
        PreconditionUtils.checkNotNull(channel);
        try {
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(BOUND_QUEUE, false, false, true, null);
            channel.queueBind(BOUND_QUEUE, EXCHANGE, ROUTING_KEY_PART);
            channel.basicPublish(EXCHANGE, ROUTING_KEY, null, message.getBytes());
            LOG.info("Message '{}' successfully published to exchange '{}' with routingKey '{}'", message, EXCHANGE, ROUTING_KEY);
        } catch (IOException e) {
            LOG.error("Producing message '{}' failed", message, channel);
        } finally {
            RabbitMqUtils.close(channel);
        }
    }

    @Override
    public String consume() {
        Channel channel = RabbitMqUtils.createChannel();
        PreconditionUtils.checkNotNull(channel);
        try {
            SingleDeliveryConsumer consumer = new SingleDeliveryConsumer(channel);
            channel.basicConsume(BOUND_QUEUE, true, consumer);
            return consumer.awaitResult(5L, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.error("Consuming from queue '{}' failed", BOUND_QUEUE, e);
            return null;
        } finally {
            RabbitMqUtils.close(channel);
        }
    }
}
