package com.finitess.rabbitmqexamples.fanout;

import com.finitess.rabbitmqexamples.common.RabbitMqUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

import static org.awaitility.Awaitility.await;

public class ExchangeExampleTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeExampleTestBase.class);
    private static final String DOCKER_FILE = System.getProperty("user.dir") + "/docker-compose.yaml";

    @BeforeAll
    public static void setUpClass() throws IOException {
        Runtime.getRuntime().exec(String.format("docker-compose -f %s up -d", DOCKER_FILE));
        LOG.info("RabbitMQ docker container starting...");
        awaitMqStarted();
        LOG.info("RabbitMQ operational");
    }

    @AfterAll
    public static void tearDownClass() throws IOException {
        Runtime.getRuntime().exec(String.format("docker-compose -f %s down -v --remove-orphans", DOCKER_FILE));
        LOG.info("RabbitMQ docker container stopping...");
        awaitMqStopped();
        LOG.info("RabbitMQ shutdown");
    }

    private static void awaitMqStarted() {
        await()
                .atMost(Duration.ofSeconds(60L))
                .pollInterval(Duration.ofSeconds(1L))
                .until(RabbitMqUtils::isMqReachable);
    }

    private static void awaitMqStopped() {
        await()
                .atMost(Duration.ofSeconds(60L))
                .pollInterval(Duration.ofSeconds(1L))
                .until(() -> !RabbitMqUtils.isMqReachable());
    }
}
