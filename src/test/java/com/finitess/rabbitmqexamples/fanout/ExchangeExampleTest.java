package com.finitess.rabbitmqexamples.fanout;


import com.finitess.rabbitmqexamples.common.ChannelProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Testcontainers
public class ExchangeExampleTest {

    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("rabbitmq")
            .withTag("3.7.25-management-alpine");

    @Container
    private static final RabbitMQContainer RABBIT_MQ_CONTAINER = new RabbitMQContainer(DEFAULT_IMAGE_NAME);

    private static ChannelProvider channelProvider;

    @BeforeAll
    static void beforeAll() throws Exception {
        await().atMost(Duration.ofSeconds(60L))
                .pollInterval(Duration.ofSeconds(1L))
                .until(RABBIT_MQ_CONTAINER::isRunning);

        channelProvider = new ChannelProvider(RABBIT_MQ_CONTAINER.getHost(), RABBIT_MQ_CONTAINER.getAmqpPort());
    }

    @Test
    public void fanoutExchange_whenMessageSent_shouldBeConsumed() throws IOException {
        testExchange(new FanoutExchangeExample(channelProvider));
    }

    @Test
    public void directExchange_whenMessageSent_shouldBeConsumed() throws IOException {
        testExchange(new DirectExchangeExample(channelProvider));
    }

    @Test
    public void topicExchange_whenMessageSent_shouldBeConsumed() throws IOException {
        testExchange(new TopicExchangeExample(channelProvider));
    }

    private static void testExchange(final ExchangeExample exchangeExample) throws IOException {
        final String sampleMessage = "Sample message";
        exchangeExample.produce(sampleMessage);
        assertThat(exchangeExample.consume()).isEqualTo(sampleMessage);
    }
}
