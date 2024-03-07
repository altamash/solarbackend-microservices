package com.solaramps.cloud.gateway.filter;

import com.solaramps.cloud.gateway.constants.SecurityConstants;
import com.solaramps.cloud.gateway.feignInterface.AuthInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;

@RefreshScope
@Component
public class JwtRequestFilter implements GatewayFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final AuthInterface authInterface;

    public JwtRequestFilter(AuthInterface authInterface) {
        this.authInterface = authInterface;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        final List<String> apiEndpoints = List.of(
                "/auth/v3/api-docs", "/commons/v3/api-docs", "/docu/v3/api-docs", "/mpo/v3/api-docs", "/mpo/crd",
                "/projectmanagement/v3/api-docs","/chatmanagement/v3/api-docs","/chatmanagement/testchat","/chatmanagement/**", "/commons/notifications/channels/email/callBack", "/commons/queue/notification",
                "auth/signup", "auth/login", "/signin",
                "/ping",
                "/mpo/ping", "/mpo/getMppWithCount", "/mpo/showMarketPlaceListings", "/mpo/showOfferDetails");
        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));
        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey(SecurityConstants.AUTHORIZATION_HEADER)) {
                return this.onError(exchange, SecurityConstants.AUTHORIZATION_HEADER, HttpStatus.UNAUTHORIZED);
            }
            List<String> requestTokenHeaders = request.getHeaders().get(SecurityConstants.AUTHORIZATION_HEADER);
            String requestTokenHeader;
            if (!requestTokenHeaders.isEmpty()) {
                requestTokenHeader = request.getHeaders().get(SecurityConstants.AUTHORIZATION_HEADER).get(0);
            } else {
                requestTokenHeader = null;
            }
            if (requestTokenHeader != null && requestTokenHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
                String jwtToken = requestTokenHeader.substring(7);
                Map<String, String> credentials = new HashMap<>();
                credentials.put("token", jwtToken);
                try {
                    if (validateToken(credentials)) {
                        return chain.filter(exchange);
                    } else {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }
                } catch (ExecutionException | InterruptedException e) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            } else {
                LOGGER.error("Authentication Failed.");
                return this.onError(exchange, SecurityConstants.COMP_KEY, HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }

    private boolean validateToken(Map<String, String> request) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(() -> authInterface.validateToken(request));
        return (Boolean) future.get();
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus)  {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
