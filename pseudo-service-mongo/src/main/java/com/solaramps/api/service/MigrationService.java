package com.solaramps.api.service;

public interface MigrationService {

    String fromSqlSAASToMongoSAAS(String type);
    String variants();
}
