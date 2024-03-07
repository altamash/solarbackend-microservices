package com.solaramps.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MongoConfigProperties {
//    @Value("${spring.data.mongodb.database:global}")
    @Value("${spring.data.mongodb.primary.name}")
    private String dataBaseName;

    @Value("${spring.data.mongodb.primary.uri}")
    public String mongoUri;

    public String getDataBaseName() {
        return dataBaseName;
    }

    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    public String getMongoURI() {
        return mongoUri;
    }

    public void setMongoURI(String mongoUri) {
        this.mongoUri = mongoUri;
    }
}
