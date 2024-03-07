package com.solaramps.loginservice.configuration;

import com.solaramps.loginservice.saas.configuration.DBContextHolder;
import com.solaramps.loginservice.saas.model.tenant.MasterTenant;
import com.solaramps.loginservice.saas.repository.MasterTenantRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getHeader("test-header") != null) {
            System.out.println(request.getHeader("test-header"));
        }
        if (request.getAttribute("test-attribute") != null) {
            System.out.println(request.getAttribute("test-attribute"));
        }
        if (request.getAttribute("Comp-Key") != null) {
            System.out.println(request.getAttribute("Comp-Key"));
        }

        String currentCompKey = DBContextHolder.getTenantName(); // check this
        String compKey = request.getHeader("Comp-Key");
//        Object val = ((MutableHttpServletRequest) request).getHeader("Comp-Key");
//        Object val2 = ((MyServletRequestWrapper) request).getHeader("Comp-Key");
        if (compKey != null) {
            MasterTenantRepository masterTenantRepository = SpringContextHolder.getApplicationContext().getBean(MasterTenantRepository.class);
            MasterTenant masterTenant = masterTenantRepository.findByCompanyKey(Long.valueOf(compKey));
            DBContextHolder.setTenantName(masterTenant.getDbName());
        }
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
//        DBContextHolder.setTenantName(null);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception exception) throws Exception {
//        DBContextHolder.setTenantName(null);
    }
}
