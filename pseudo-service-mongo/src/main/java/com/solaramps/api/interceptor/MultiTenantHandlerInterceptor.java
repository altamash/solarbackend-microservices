package com.solaramps.api.interceptor;

import com.solaramps.api.TenantHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class MultiTenantHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private TenantHolder tenantHolder;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        setTenantId(request, tenantHolder);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        tenantHolder.clear();
    }

    private void setTenantId(HttpServletRequest request, TenantHolder tenantHolder) {
        // Header that would be attached to every request made to identify tenant DB.
        tenantHolder.setTenantId(request.getHeader("Tenant-Id"));
    }
}

