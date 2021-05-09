package com.finitess.rabbitmqexamples.fanout;

import com.finitess.rabbitmqexamples.common.ChannelProvider;
import com.finitess.rabbitmqexamples.common.SingleDeliveryConsumer;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public abstract class ExchangeExample {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeExample.class);

    private final ChannelProvider channelProvider;

    protected ExchangeExample(final ChannelProvider channelProvider) {
        this.channelProvider = channelProvider;
    }

    public abstract void produce(String message) throws IOException;

    public abstract String getBoundQueue();

    public String consume() throws IOException {
        final Channel channel = channelProvider.provide();
        try {
            final SingleDeliveryConsumer consumer = new SingleDeliveryConsumer(channel);
            channel.basicConsume(getBoundQueue(), true, consumer);
            return consumer.awaitResult(5L, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.error("Consuming from queue '{}' failed", getBoundQueue(), e);
            return null;
        }
    }
}
