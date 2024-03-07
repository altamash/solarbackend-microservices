package com.solaramps.api.commons.saas.configuration;

import com.solaramps.api.commons.constants.AppConstants;
import com.solaramps.api.commons.saas.model.SaasSchema;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties("multitenancy.saas.datasource")
@Getter
@Setter
public class MasterDatabaseConfigProperties {

    private String connectionString;
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private long connectionTimeout;
    private int maxPoolSize;
    private long idleTimeout;
    private int minIdle;
    private long leakDetectionThreshold;
    private String poolName;

    @PostConstruct
    public void setUrlAndCredential() {
        String schemaName = SaasSchema.SAAS_SCHEMA_NAME;
        Map<String, String> connectionMap = new HashMap<>();
        List<String> values =
                Arrays.stream(connectionString.split(";")).map(string -> string.trim()).collect(Collectors.toList());
        values.stream().forEach(value -> {
            String[] valueParts = value.split("=");
            connectionMap.put(valueParts[0], valueParts[1]);
        });
        setUrl(AppConstants.API_DATABASE_PREFIX + connectionMap.get("Data Source") + "/" + schemaName + "?reconnect" +
                "=true&createDatabaseIfNotExist=true&zeroDateTimeBehavior=convertToNull&serverTimezone=UTC");
        setUsername(connectionMap.get("User Id"));
        setPassword(connectionMap.get("Password"));
    }

    //Initialization of HikariCP.
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MasterDatabaseConfigProperties [url=");
        builder.append(url);
        builder.append(", username=");
        builder.append(username);
        builder.append(", password=");
        builder.append(password);
        builder.append(", driverClassName=");
        builder.append(driverClassName);
        builder.append(", connectionTimeout=");
        builder.append(connectionTimeout);
        builder.append(", maxPoolSize=");
        builder.append(maxPoolSize);
        builder.append(", idleTimeout=");
        builder.append(idleTimeout);
        builder.append(", minIdle=");
        builder.append(minIdle);
        builder.append(", leakDetectionThreshold=");
        builder.append(leakDetectionThreshold);
        builder.append(", poolName=");
        builder.append(poolName);
        builder.append("]");
        return builder.toString();
    }
}
