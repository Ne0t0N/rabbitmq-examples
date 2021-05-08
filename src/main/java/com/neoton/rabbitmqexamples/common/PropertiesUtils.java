package com.neoton.rabbitmqexamples.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtils.class);
    private static final Properties properties = new Properties();

    static {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("application.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            LOG.error("Properties could not be read, exiting application", e);
            System.exit(1);
        }
    }

    private PropertiesUtils() {
    }

    public static Properties getProperties() {
        return properties;
    }
}
