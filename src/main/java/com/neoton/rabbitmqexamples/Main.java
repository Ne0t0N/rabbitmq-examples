package com.neoton.rabbitmqexamples;

import com.neoton.rabbitmqexamples.fanout.FanoutExchangeExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ExchangeExample exchangeExample = new FanoutExchangeExample();
        exchangeExample.produce("Hello world");
        String consume = exchangeExample.consume();
        LOG.info("Consumed message: '{}'", consume);
    }
}
