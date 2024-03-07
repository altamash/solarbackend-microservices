package com.solaramps.api.it.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
@EnableDiscoveryClient // use with feign client
@EnableFeignClients
public class DocumentManagementApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentManagementApplication.class);

    static {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, System.getenv("PROFILE") != null ? System.getenv("PROFILE") : "local");
    }

    public static void main(String[] args) {
        try {
            SpringApplication.run(DocumentManagementApplication.class, args);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
