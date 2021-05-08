package com.neoton.rabbitmqexamples.common;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PreconditionUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PreconditionUtils.class);

    private PreconditionUtils() {
    }

    public static void checkNotNull(Channel channel) {
        if (channel == null) {
            String errorMessage = "Will not proceed with exchange, channel is null";
            LOG.error(errorMessage);
            throw new NullPointerException(errorMessage);
        }
    }
}
