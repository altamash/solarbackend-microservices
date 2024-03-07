package com.solaramps.api.config;

import com.solaramps.api.SharedCollection;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com"},
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = SharedCollection.class),
        mongoTemplateRef = "mongoTemplateShared"
)
public class SharedMongoConfig {

}

