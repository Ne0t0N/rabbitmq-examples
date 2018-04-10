package com.neoton.rabbitmqexamples;

public interface ExchangeExample {

    void produce(String message);
    String consume();
}
