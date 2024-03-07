package com.solaramps.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import java.util.Arrays;

@SpringBootApplication(exclude = {
		MongoAutoConfiguration.class,
		MongoDataAutoConfiguration.class
})
@EnableEurekaClient
public class MongopocApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(MongopocApplication.class);

	static {
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, System.getenv("PROFILE") != null ? System.getenv("PROFILE") : "local");
	}
	public static void main(String[] args) {
		try {
			ApplicationContext ctx = SpringApplication.run(MongopocApplication.class, args);
			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
