package com.finitess.rabbitmqexamples.fanout;

import com.finitess.rabbitmqexamples.common.PreconditionUtils;
import com.finitess.rabbitmqexamples.common.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FanoutExchangeExample extends ExchangeExample {

    private static final Logger LOG = LoggerFactory.getLogger(FanoutExchangeExample.class);
    private static final String EXCHANGE = "sample.fanout";
    private static final String BOUND_QUEUE = "sample.fanout.bound.queue";

    @Override
    public void produce(String message) {
        final Channel channel = RabbitMqUtils.createChannel();
        PreconditionUtils.checkNotNull(channel);
        try {
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.FANOUT);
            channel.queueDeclare(BOUND_QUEUE, false, false, true, null);
            channel.queueBind(BOUND_QUEUE, EXCHANGE, "");
            channel.basicPublish(EXCHANGE, "", null, message.getBytes());
            LOG.info("Message '{}' successfully published to exchange '{}'", message, EXCHANGE);
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
