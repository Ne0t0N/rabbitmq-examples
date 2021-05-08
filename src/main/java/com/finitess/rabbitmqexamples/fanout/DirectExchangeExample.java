package com.finitess.rabbitmqexamples.fanout;

import com.finitess.rabbitmqexamples.common.PreconditionUtils;
import com.finitess.rabbitmqexamples.common.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DirectExchangeExample extends ExchangeExample {

    private static final Logger LOG = LoggerFactory.getLogger(FanoutExchangeExample.class);
    private static final String EXCHANGE = "sample.direct";
    private static final String BOUND_QUEUE = "sample.direct.bound.queue";
    private static final String ROUTING_KEY = "sample.direct.routing.key";

    @Override
    public void produce(String message) {
        final Channel channel = RabbitMqUtils.createChannel();
        PreconditionUtils.checkNotNull(channel);
        try {
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(BOUND_QUEUE, false, false, true, null);
            channel.queueBind(BOUND_QUEUE, EXCHANGE, ROUTING_KEY);
            channel.basicPublish(EXCHANGE, ROUTING_KEY, null, message.getBytes());
            LOG.info("Message '{}' successfully published to exchange '{}' with routingKey '{}'", message, EXCHANGE, ROUTING_KEY);
        } catch (IOException e) {
            LOG.error("Producing message '{}' for channel '{}' failed", message, channel);
        } finally {
            RabbitMqUtils.close(channel);
        }
    }

    @Override
    public String getBoundQueue() {
        return BOUND_QUEUE;
    }
}
