package com.finitess.rabbitmqexamples.fanout;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExchangeExampleTest extends ExchangeExampleTestBase {

    private static final String SAMPLE_MESSAGE = "Sample message";

    @Test
    public void fanoutExchange_whenMessageSent_shouldBeConsumed() {
        testExchange(new FanoutExchangeExample());
    }

    @Test
    public void directExchange_whenMessageSent_shouldBeConsumed() {
        testExchange(new DirectExchangeExample());
    }

    @Test
    public void topicExchange_whenMessageSent_shouldBeConsumed() {
        testExchange(new TopicExchangeExample());
    }

    private static void testExchange(ExchangeExample exchangeExample) {
        exchangeExample.produce(SAMPLE_MESSAGE);
        assertThat(exchangeExample.consume()).isEqualTo(SAMPLE_MESSAGE);
    }
}
