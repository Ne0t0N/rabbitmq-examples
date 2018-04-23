package com.neoton.rabbitmqexamples.fanout;

public interface ExchangeExample {

    void produce(String message);
    String consume();
}
