package com.finitess.rabbitmqexamples.plain.common;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ChannelProvider implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(ChannelProvider.class);

    private final Connection connection;

    public ChannelProvider(final String rabbitMqHost, final Integer rabbitMqPort) throws Exception {
        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitMqHost);
        connectionFactory.setPort(rabbitMqPort);
        this.connection = connectionFactory.newConnection();

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public Channel provide() throws IOException {
        final Channel channel = connection.createChannel();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> closeChannel(channel)));
        return channel;
    }

    @Override
    public void close() {
        try {
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (IOException e) {
            LOG.error("Could not properly close connection", e);
        }
    }

    private void closeChannel(final Channel channel) {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        } catch (Exception e) {
            LOG.error("Could not properly close connection", e);
        }
    }
}
