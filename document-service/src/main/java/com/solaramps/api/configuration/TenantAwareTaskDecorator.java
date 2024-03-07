package com.solaramps.api.configuration;

import com.solaramps.api.saas.configuration.DBContextHolder;
import org.springframework.core.task.TaskDecorator;

public class TenantAwareTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        String tenantName = DBContextHolder.getTenantName();
        return () -> {
            try {
                DBContextHolder.setTenantName(tenantName);
                runnable.run();
            } finally {
                DBContextHolder.setTenantName(null);
            }
        };
    }
}
