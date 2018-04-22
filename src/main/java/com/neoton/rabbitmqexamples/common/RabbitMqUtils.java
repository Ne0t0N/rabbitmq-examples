package com.neoton.rabbitmqexamples.common;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public final class RabbitMqUtils {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMqUtils.class);
    private static final String RABBITMQ_HOST;
    private static final String RABBITMQ_PORT;
    private static final ConnectionFactory CONNECTION_FACTORY = new ConnectionFactory();

    private static Connection connection;

    static {
        Properties properties = PropertiesUtils.getProperties();
        RABBITMQ_HOST = properties.getProperty("rabbitmq.host");
        RABBITMQ_PORT = properties.getProperty("rabbitmq.port");

        CONNECTION_FACTORY.setHost(RABBITMQ_HOST);
        CONNECTION_FACTORY.setPort(Integer.valueOf(RABBITMQ_PORT));
    }

    private RabbitMqUtils() {
    }

    public static Channel createChannel() {
        try {
            if (connection == null || !connection.isOpen()) {
                connection = CONNECTION_FACTORY.newConnection();
            }
            return connection.createChannel();
        } catch (Exception e) {
            LOG.error("Could not create channel for connection '{}', socket '{}:{}'", connection, RABBITMQ_HOST, RABBITMQ_PORT, e);
            return null;
        }
    }

    public static void close(Channel channel) {
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
