package com.solaramps.api.commons.configuration;

import com.solaramps.api.commons.saas.configuration.DBContextHolder;
import com.solaramps.api.commons.saas.model.tenant.MasterTenant;
import com.solaramps.api.commons.saas.repository.MasterTenantRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
//@DependsOn("masterTenantRepository")
//@Lazy
public class RequestFilter extends OncePerRequestFilter {

//    @Autowired(required = false)
//    private MasterTenantRepository masterTenantRepository;

//    public RequestInterceptor(MasterTenantRepository masterTenantRepository) {
//        this.masterTenantRepository = masterTenantRepository;
//    }
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestFilter.class);
    private static final String[] WHITELIST_API = { "/v3/api-docs", "/queue/notification",
            "/commons/notifications/channels/email/callBack", "/commons/commons/notifications/channels/email/callBack" };
    private final JwtTokenUtil jwtTokenUtil;

    public RequestFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getAttribute("Comp-Key") != null) {
            System.out.println(request.getAttribute("Comp-Key"));
        }
        String tenantId = request.getHeader("Comp-Key");
        if (tenantId == null) {
            String requestTokenHeader = request.getHeader("Authorization");
            String jwtToken;
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    tenantId = jwtTokenUtil.getTenantClientIdFromToken(jwtToken);
                } catch (Exception e) {
                    LOGGER.error("Authentication Failed. Username or Password not valid.", e);
                }
            }
        }
        if (tenantId != null) {
            MasterTenantRepository masterTenantRepository = SpringContextHolder.getApplicationContext().getBean(MasterTenantRepository.class);
            MasterTenant masterTenant = masterTenantRepository.findByCompanyKey(Long.valueOf(tenantId));
            DBContextHolder.setTenantName(masterTenant.getDbName());
        } else if (!List.of(WHITELIST_API).contains(request.getRequestURI())) {
            throw new RuntimeException("Access is Denied. Please login again or contact service provider");
        }
        filterChain.doFilter(request, response);
    }
}
