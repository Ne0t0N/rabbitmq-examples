package com.neoton.rabbitmqexamples.fanout;

import com.neoton.rabbitmqexamples.common.PreconditionUtils;
import com.neoton.rabbitmqexamples.common.RabbitMqUtils;
import com.neoton.rabbitmqexamples.common.SingleDeliveryConsumer;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class ExchangeExample {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeExample.class);

    public abstract void produce(String message);

    public abstract String getBoundQueue();

    public String consume() {
        Channel channel = RabbitMqUtils.createChannel();
        PreconditionUtils.checkNotNull(channel);
        try {
            SingleDeliveryConsumer consumer = new SingleDeliveryConsumer(channel);
            channel.basicConsume(getBoundQueue(), true, consumer);
            return consumer.awaitResult(5L, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.error("Consuming from queue '{}' failed", getBoundQueue(), e);
            return null;
        } finally {
            RabbitMqUtils.close(channel);
        }
    }
}
