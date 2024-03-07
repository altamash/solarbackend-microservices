package com.solaramps.loginservice.tenant.controller.v1;

import com.solaramps.loginservice.configuration.jwt.JwtTokenUtil;
import com.solaramps.loginservice.constants.WebResourceKeyConstants;
import com.solaramps.loginservice.saas.model.tenant.MasterTenant;
import com.solaramps.loginservice.saas.repository.MasterTenantRepository;
import com.solaramps.loginservice.tenant.mapper.requestDTO.JwtRequest;
import com.solaramps.loginservice.tenant.mapper.user.UserDTO;
import com.solaramps.loginservice.tenant.service.LoginService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = WebResourceKeyConstants.AUTH_CONTROLLER)
@Slf4j
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    private final LoginService loginService;
    private final MasterTenantRepository masterTenantRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public LoginController(LoginService loginService, MasterTenantRepository masterTenantRepository, JwtTokenUtil jwtTokenUtil) {
        this.loginService = loginService;
        this.masterTenantRepository = masterTenantRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(value = WebResourceKeyConstants.LOGIN)
    public UserDTO loginUser(@RequestBody JwtRequest requestDTO, @RequestHeader("Comp-Key") Long compKey,
                             HttpServletRequest request) throws Exception {
        return loginService.login(requestDTO, compKey, request);
    }

    @PostMapping("/validate")
    @Hidden
    public boolean validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        if (token == null || token.isEmpty()) {
            return false;
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        String audience = jwtTokenUtil.getTenantClientIdFromToken(token);
        MasterTenant masterTenant = masterTenantRepository.findById(Long.valueOf(audience)).orElse(null);
        if (null == masterTenant) {
            LOGGER.error("An error during getting tenant name");
            return false;
        }
        return jwtTokenUtil.validateToken(request.get("token"), username);
    }
}
