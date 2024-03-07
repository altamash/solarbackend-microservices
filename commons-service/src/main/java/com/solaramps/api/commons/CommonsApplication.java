package com.solaramps.api.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
@EnableDiscoveryClient
public class CommonsApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonsApplication.class);
    private static long APP_START_MILLIS = System.currentTimeMillis();

    static {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, System.getenv("PROFILE") != null ? System.getenv("PROFILE") : "local");
    }

    public static void main(String[] args) {
        try {
            SpringApplication.run(CommonsApplication.class, args);
            String appStartupTime = Utility.getFormattedMillis(System.currentTimeMillis() - APP_START_MILLIS);
            LOGGER.info("App startup time: {}", appStartupTime);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /*@Bean
    public ApplicationRunner runner(KafkaTemplate<String, String> template) {
        return args -> {
            template.send("alert-topic", "test");
        };
    }*/
}
