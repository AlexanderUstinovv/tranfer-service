package com.revolut.transferservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public enum ApplicationProperties {
    INSTANCE;

    ApplicationProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(ApplicationProperties.class.getResourceAsStream(path));
            } catch (Exception ex) {
                logger.error("Unable to load " + path + " file from classpath.", ex);
            }
        }
    }

    public String getValue(String propertyName) {
        String value = null;
        if (properties != null) {
            value =  properties.getProperty(propertyName);
        }
        return value;
    }

    private Properties properties;

    private final Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);
    private final String path = "/application.properties";
}
