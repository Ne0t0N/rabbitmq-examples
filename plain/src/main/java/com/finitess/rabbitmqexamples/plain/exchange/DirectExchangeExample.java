package com.finitess.rabbitmqexamples.plain.exchange;

import com.finitess.rabbitmqexamples.plain.common.ChannelProvider;
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

    private final ChannelProvider channelProvider;

    public DirectExchangeExample(final ChannelProvider channelProvider) {
        super(channelProvider);
        this.channelProvider = channelProvider;
    }

    @Override
    public void produce(final String message) throws IOException {
        final Channel channel = channelProvider.provide();
        try {
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(BOUND_QUEUE, false, false, true, null);
            channel.queueBind(BOUND_QUEUE, EXCHANGE, ROUTING_KEY);
            channel.basicPublish(EXCHANGE, ROUTING_KEY, null, message.getBytes());
            LOG.info("Message '{}' successfully published to exchange '{}' with routingKey '{}'", message, EXCHANGE, ROUTING_KEY);
        } catch (IOException e) {
            LOG.error("Producing message '{}' for channel '{}' failed", message, channel);
        }
    }

    @Override
    public String getBoundQueue() {
        return BOUND_QUEUE;
    }
}
