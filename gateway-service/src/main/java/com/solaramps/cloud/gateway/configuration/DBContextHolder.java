package com.solaramps.cloud.gateway.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class DBContextHolder implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBContextHolder.class);
    private static ApplicationContext applicationContext;
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setTenantName(String dbType) {
        contextHolder.set(dbType);
    }

    public static String getTenantName() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DBContextHolder.applicationContext = applicationContext;
    }
}
