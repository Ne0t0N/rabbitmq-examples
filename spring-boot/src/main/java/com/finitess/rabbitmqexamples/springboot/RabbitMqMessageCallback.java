package com.finitess.rabbitmqexamples.springboot;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Component
public class RabbitMqMessageCallback implements Consumer<String> {

    private final List<String> messages = new ArrayList<>();

    @Override
    public void accept(final String message) {
        messages.add(message);
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}
