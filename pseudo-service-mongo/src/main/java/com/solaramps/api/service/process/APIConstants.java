package com.solaramps.api.service.process;

import org.springframework.http.HttpMethod;

public interface APIConstants {
    HttpMethod getMethod();
    String getUrl();
    String getUrlSuffix();
}
