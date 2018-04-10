package com.neoton.rabbitmqexamples.common;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public final class RabbitMqUtils {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMqUtils.class);
    private static final String RABBITMQ_HOST;
    private static final String RABBITMQ_PORT;
    private static final ConnectionFactory CONNECTION_FACTORY = new ConnectionFactory();

    static {
        Properties properties = PropertiesUtils.getProperties();
        RABBITMQ_HOST = properties.getProperty("rabbitmq.host");
        RABBITMQ_PORT = properties.getProperty("rabbitmq.port");

        CONNECTION_FACTORY.setHost(RABBITMQ_HOST);
    }

    private RabbitMqUtils() {
    }

    public static Connection connect() {
        try {
            return CONNECTION_FACTORY.newConnection();
        } catch (Exception e) {
            LOG.error("Connection to '{}:{}' could not be established", RABBITMQ_HOST, RABBITMQ_PORT, e);
            return null;
        }
    }

    public static Channel createChannel(Connection connection) {
        try {
            return connection.createChannel();
        } catch (IOException e) {
            LOG.error("Could not create channel for connection '{}'", connection, e);
            return null;
        }
    }

    public static void close(Connection connection, Channel channel) {
        try {
            if (channel.isOpen()) {
                channel.close();
            }
            if (connection.isOpen()) {
                connection.close();
            }
        } catch (Exception e) {
            LOG.error("Could not properly close connection/channel, still open ({}/{})", connection.isOpen(), channel.isOpen(), e);
        }
    }
}
