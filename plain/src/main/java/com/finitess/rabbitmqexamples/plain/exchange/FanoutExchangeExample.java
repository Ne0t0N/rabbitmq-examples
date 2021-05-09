package com.finitess.rabbitmqexamples.plain.exchange;

import com.finitess.rabbitmqexamples.plain.common.ChannelProvider;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FanoutExchangeExample extends ExchangeExample {

    private static final Logger LOG = LoggerFactory.getLogger(FanoutExchangeExample.class);
    private static final String EXCHANGE = "sample.fanout";
    private static final String BOUND_QUEUE = "sample.fanout.bound.queue";

    private final ChannelProvider channelProvider;

    public FanoutExchangeExample(final ChannelProvider channelProvider) {
        super(channelProvider);
        this.channelProvider = channelProvider;
    }

    @Override
    public void produce(final String message) throws IOException {
        final Channel channel = channelProvider.provide();
        try {
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.FANOUT);
            channel.queueDeclare(BOUND_QUEUE, false, false, true, null);
            channel.queueBind(BOUND_QUEUE, EXCHANGE, "");
            channel.basicPublish(EXCHANGE, "", null, message.getBytes());
            LOG.info("Message '{}' successfully published to exchange '{}'", message, EXCHANGE);
        } catch (IOException e) {
            LOG.error("Producing message '{}' for channel '{}' failed", message, channel);
        }
    }

    @Override
    public String getBoundQueue() {
        return BOUND_QUEUE;
    }
}
