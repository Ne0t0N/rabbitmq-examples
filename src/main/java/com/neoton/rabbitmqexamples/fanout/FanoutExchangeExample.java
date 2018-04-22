package com.neoton.rabbitmqexamples.fanout;

import com.neoton.rabbitmqexamples.ExchangeExample;
import com.neoton.rabbitmqexamples.common.RabbitMqUtils;
import com.neoton.rabbitmqexamples.common.SingleDeliveryConsumer;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FanoutExchangeExample implements ExchangeExample {

    private static final Logger LOG = LoggerFactory.getLogger(FanoutExchangeExample.class);
    private static final String EXCHANGE = "sample.fanout";
    private static final String BOUND_QUEUE = "sample.fanout.bound.queue";

    @Override
    public void produce(String message) {
        Channel channel = RabbitMqUtils.createChannel();
        if (channel == null) {
            LOG.error("Will not proceed with exchange, channel is null");
            return;
        }

        try {
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.FANOUT);
            channel.queueDeclare(BOUND_QUEUE, false, false, true, null);
            channel.queueBind(BOUND_QUEUE, EXCHANGE, "");
            channel.basicPublish(EXCHANGE, "", null, message.getBytes());
            LOG.info("Message '{}' successfully published to exchange '{}'", message, EXCHANGE);
        } catch (IOException e) {
            LOG.error("Producing message '{}' failed", message, channel);
        } finally {
            RabbitMqUtils.close(channel);
        }
    }

    @Override
    public String consume() {
        Channel channel = RabbitMqUtils.createChannel();
        if (channel == null) {
            LOG.error("Will not proceed with exchange, channel is null");
            return null;
        }

        try {
            SingleDeliveryConsumer consumer = new SingleDeliveryConsumer(channel);
            channel.basicConsume(BOUND_QUEUE, true, consumer);
            consumer.await(5L, TimeUnit.SECONDS);
            return consumer.getResult();
        } catch (Exception e) {
            LOG.error("Consuming from queue '{}' failed", BOUND_QUEUE, e);
            return null;
        } finally {
            RabbitMqUtils.close(channel);
        }
    }
}
