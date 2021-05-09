package com.finitess.rabbitmqexamples.common;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SingleDeliveryConsumer extends DefaultConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(SingleDeliveryConsumer.class);

    private final CountDownLatch countDownLatch;
    private final AtomicReference<String> result;

    public SingleDeliveryConsumer(final Channel channel) {
        super(channel);
        this.countDownLatch = new CountDownLatch(1);
        this.result = new AtomicReference<>();
    }

    @Override
    public void handleDelivery(final String consumerTag,
                               final Envelope envelope,
                               final AMQP.BasicProperties properties,
                               final byte[] body) {
        final String message = new String(body, StandardCharsets.UTF_8);
        result.set(message);
        countDownLatch.countDown();
        LOG.info("Received message '{}' with consumerType '{}' from exchange '{}'", message, consumerTag, envelope.getExchange());
    }

    public String awaitResult(final long timeout, final TimeUnit timeoutUnit) throws InterruptedException {
        if (countDownLatch.await(timeout, timeoutUnit)) {
            return result.get();
        } else {
            return null;
        }
    }
}
