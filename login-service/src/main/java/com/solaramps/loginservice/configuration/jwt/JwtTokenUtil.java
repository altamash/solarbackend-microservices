package com.solaramps.loginservice.configuration.jwt;


import com.solaramps.loginservice.saas.model.tenant.MasterTenant;
import com.solaramps.loginservice.saas.service.MasterTenantService;
import com.solaramps.loginservice.saas.service.encryption.PEMUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtTokenUtil implements Serializable {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final long serialVersionUID = -2550185165626007488L;

    @Autowired
    private PEMUtils pemUtils;
    @Autowired
    private MasterTenantService masterTenantService;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getTenantClientIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getAudience);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(JwtConstants.SIGNING_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails, String tenantId, Integer tenantTier) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", userDetails);
        /*String privateKeyPEMString = pemUtils.getPEMString("RSA", PrivateKey.class);
        if (!StringUtils.isEmpty(privateKeyPEMString)) {
            claims.put("ppk", Base64.getEncoder().encodeToString(privateKeyPEMString.substring(0, 10).getBytes()));
        }*/
        claims.put("tier", tenantTier);
        return doGenerateToken(claims, userDetails.getUsername(), tenantId);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, String tenantId) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // Compared for validation
                .setAudience(tenantId) // Compared for validation
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, JwtConstants.SIGNING_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        return validateToken(token, userDetails.getUsername());
    }

    public Boolean validateToken(String token, String loginUser) {
        LOGGER.info("Validation for " + loginUser);
        final String username = getUsernameFromToken(token);
        return (username.equals(loginUser) && !isTokenExpired(token));
    }

    public MasterTenant getMasterTenant(String authorization) {
        MasterTenant masterTenant = null;
        String audience;
        String jwtToken = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            jwtToken = authorization.substring(7);
            try {
                audience = getTenantClientIdFromToken(jwtToken);
                masterTenant = masterTenantService.findById(Long.valueOf(audience));
                if (null == masterTenant) {
                    LOGGER.error("An error during getting tenant name");
                    throw new BadCredentialsException("Invalid tenant and user.");
                }
            } catch (IllegalArgumentException e) {
                LOGGER.error("Unable to get JWT Token", e);
            } catch (ExpiredJwtException e) {
                LOGGER.error("JWT Token has expired", e);
            }
        }
        return masterTenant;
    }
}
