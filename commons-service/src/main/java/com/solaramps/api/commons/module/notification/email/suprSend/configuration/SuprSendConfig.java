package com.solaramps.api.commons.module.notification.email.suprSend.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solaramps.api.commons.module.notification.email.suprSend.mapper.AndroidPushDTO;
import com.solaramps.api.commons.module.notification.email.suprSend.mapper.SlackIdentDTO;
import com.solaramps.api.commons.module.notification.email.suprSend.mapper.WebPushDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import suprsend.Suprsend;
import suprsend.SuprsendException;

//@Profile("local")
@Configuration
public class SuprSendConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuprSendConfig.class);

    @Value(value = "${notification.suprSend.workSpaceKey}")
    private String workSpaceKey;
    @Value(value = "${notification.suprSend.workSpaceSecret}")
    private String workSpaceSecret;

    @Bean
    public Suprsend suprsend() {
        try {
            return new Suprsend(workSpaceKey, workSpaceSecret, true);
        } catch (SuprsendException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Converter<String, SlackIdentDTO> stringToSlackIdentDTOConverter() {
        return new Converter<>() {
            @Override
            public SlackIdentDTO convert(String str) {
                try {
                    return new ObjectMapper().readValue(str, SlackIdentDTO.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Bean
    public Converter<String, WebPushDTO> stringToWebPushDTOConverter() {
        return new Converter<>() {
            @Override
            public WebPushDTO convert(String str) {
                try {
                    return new ObjectMapper().readValue(str, WebPushDTO.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Bean
    public Converter<String, AndroidPushDTO> stringToAndroidPushDTOConverter() {
        return new Converter<>() {
            @Override
            public AndroidPushDTO convert(String str) {
                try {
                    return new ObjectMapper().readValue(str, AndroidPushDTO.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
