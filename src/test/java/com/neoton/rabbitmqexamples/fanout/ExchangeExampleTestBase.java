package com.neoton.rabbitmqexamples.fanout;

import com.neoton.rabbitmqexamples.common.RabbitMqUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ExchangeExampleTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeExampleTestBase.class);
    private static final String DOCKER_FILE = System.getProperty("user.dir") + "/docker-compose.yaml";

    @BeforeClass
    public static void setUpClass() throws IOException, InterruptedException {
        Runtime.getRuntime().exec(String.format("docker-compose -f %s up -d", DOCKER_FILE));
        LOG.info("RabbitMQ docker container starting...");
        awaitMqStarted();
        LOG.info("RabbitMQ operational");
    }

    @AfterClass
    public static void tearDownClass() throws IOException, InterruptedException {
        Runtime.getRuntime().exec(String.format("docker-compose -f %s down -v --remove-orphans", DOCKER_FILE));
        LOG.info("RabbitMQ docker container stopping...");
        awaitMqStopped();
        LOG.info("RabbitMQ shutdown");
    }

    private static void awaitMqStarted() throws InterruptedException {
        while (!RabbitMqUtils.isMqReachable()) {
            Thread.sleep(1_000);
        }
    }

    private static void awaitMqStopped() throws InterruptedException {
        while (RabbitMqUtils.isMqReachable()) {
            Thread.sleep(1_000);
        }
    }
}
