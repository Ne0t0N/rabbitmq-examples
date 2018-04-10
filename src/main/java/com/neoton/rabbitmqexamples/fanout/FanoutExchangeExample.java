package com.neoton.rabbitmqexamples.fanout;

import com.neoton.rabbitmqexamples.ExchangeExample;
import com.neoton.rabbitmqexamples.common.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class FanoutExchangeExample implements ExchangeExample {

    private static final Logger LOG = LoggerFactory.getLogger(FanoutExchangeExample.class);
    private static final String EXCHANGE = "sample.fanout";
    private static final String BOUND_QUEUE = "sample.fanout.bound.queue";

    @Override
    public void produce(String message) {
        Connection connection = RabbitMqUtils.connect();
        Channel channel = RabbitMqUtils.createChannel(connection);
        if (connection == null || channel == null) {
            LOG.error("Will not proceed with exchange for connection '{}' and channel '{}'", connection, channel);
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
            RabbitMqUtils.close(connection, channel);
        }
    }

    @Override
    public String consume() {
        Connection connection = RabbitMqUtils.connect();
        Channel channel = RabbitMqUtils.createChannel(connection);
        if (connection == null || channel == null) {
            LOG.error("Will not proceed with exchange for connection '{}' and channel '{}'", connection, channel);
            return null;
        }

        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final AtomicReference<String> result = new AtomicReference<>();
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    result.set(message);
                    countDownLatch.countDown();
                    LOG.info("Received message '{}' from queue '{}'", message, BOUND_QUEUE);
                }
            };
            channel.basicConsume(BOUND_QUEUE, true, consumer);
            countDownLatch.await(5L, TimeUnit.SECONDS);
            return result.get();
        } catch (Exception e) {
            LOG.error("Consuming from queue '{}' failed", BOUND_QUEUE, e);
            return null;
        } finally {
            RabbitMqUtils.close(connection, channel);
        }
    }
}
