package org.LinkedOn.server.utils;

import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private static Properties properties;

    // Reads configuration from the "app.properties" file
    private static void readConfiguration() {
        if (properties != null) return;

        try {
            properties = new Properties();
            properties.load(Configuration.class.getResourceAsStream("/app.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the configuration", e);
        }
    }

    // Retrieves a value from properties using dot-separated keys
    public static String getValue(String... keys) {
        readConfiguration();
        return (String) properties.get(String.join(".", keys));
    }
}
