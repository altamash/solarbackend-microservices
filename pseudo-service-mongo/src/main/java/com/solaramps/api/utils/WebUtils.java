package com.solaramps.api.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class WebUtils {

    public static <T> ResponseEntity<T> submitRequest(HttpMethod httpMethod, String url, Object requestBody, Map<String,
            List<String>> headers, Class responseType, Object... uriVariables) {
        // Request header
        HttpHeaders reqHeader = new HttpHeaders();
        headers.entrySet().forEach(header -> reqHeader.put(header.getKey(), header.getValue()));
        // HTTP entity object - holds header and body
        HttpEntity<String> reqEntity;
        if (requestBody != null) {
            reqEntity = new HttpEntity(requestBody, reqHeader);
        } else {
            reqEntity = new HttpEntity<>(reqHeader);
        }
        return new RestTemplate().exchange(url, httpMethod, reqEntity, responseType, uriVariables);
    }

}
