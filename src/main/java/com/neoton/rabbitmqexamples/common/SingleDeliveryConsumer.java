package com.neoton.rabbitmqexamples.common;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SingleDeliveryConsumer extends DefaultConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(SingleDeliveryConsumer.class);

    private final CountDownLatch countDownLatch;
    private final AtomicReference<String> result;

    public SingleDeliveryConsumer(Channel channel) {
        super(channel);
        this.countDownLatch = new CountDownLatch(1);
        this.result = new AtomicReference<>();
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        result.set(message);
        countDownLatch.countDown();
        LOG.info("Received message '{}' with consumerType '{}' from exchange '{}'", message, consumerTag, envelope.getExchange());
    }

    public String awaitResult(long timeout, TimeUnit timeoutUnit) throws InterruptedException {
        countDownLatch.await(timeout, timeoutUnit);
        return result.get();
    }
}
