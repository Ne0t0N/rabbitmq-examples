package com.finitess.rabbitmqexamples.springboot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {RabbitMqTest.RabbitMqInitializer.class})
public class RabbitMqTest {

    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("rabbitmq")
            .withTag("3.7.25-management-alpine");

    private static final RabbitMQContainer RABBIT_MQ_CONTAINER = new RabbitMQContainer(DEFAULT_IMAGE_NAME);

    @Autowired
    private RabbitMqMessageProducer producer;

    @Autowired
    private RabbitMqMessageCallback callback;

    @BeforeAll
    static void beforeAll() {
        RABBIT_MQ_CONTAINER.start();
        await().atMost(Duration.ofSeconds(60L))
                .pollInterval(Duration.ofSeconds(1L))
                .until(RABBIT_MQ_CONTAINER::isRunning);
    }

    @Test
    void testProduceConsume() {
        final String message = "Hello RabbitMQ";
        producer.produce(message);

        await().atMost(Duration.ofSeconds(10L))
                .pollInterval(Duration.ofMillis(100L))
                .until(() -> !callback.getMessages().isEmpty());

        assertArrayEquals(new String[]{message}, callback.getMessages().toArray());
    }

    @AfterEach
    void tearDown() {
        RABBIT_MQ_CONTAINER.stop();
    }

    public static class RabbitMqInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues
                    .of("spring.rabbitmq.host=" + RABBIT_MQ_CONTAINER.getHost(), "spring.rabbitmq.port=" + RABBIT_MQ_CONTAINER.getAmqpPort())
                    .applyTo(context);
        }
    }
}
