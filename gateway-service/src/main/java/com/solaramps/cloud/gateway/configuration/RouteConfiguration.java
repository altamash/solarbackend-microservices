package com.solaramps.cloud.gateway.configuration;

import com.solaramps.cloud.gateway.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@RefreshScope
@Configuration
public class RouteConfiguration {

    @Value("${swagger.host}")
    private String swaggerHost;
    @Value("${service.names.login}")
    private String loginServiceName;
    @Value("${service.names.commons}")
    private String commonsServiceName;
    @Value("${service.names.document}")
    private String documentServiceName;
    @Value("${service.names.mpoffer}")
    private String mpofferServiceName;
    @Value("${service.names.prjmanagement}")
    private String projectManagementServiceName;

    @Autowired
    @Lazy
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public RouteLocator apiRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(loginServiceName, p -> p
                        .path("/auth/**")
                        .filters(f -> f.filter(jwtRequestFilter)
                                .circuitBreaker(config -> config
                                        .setName(loginServiceName)
                                        .setFallbackUri("forward:/loginServiceFallBack"))
                                .rewritePath("/auth/(?<path>.*)", "/${path}"))
                        .uri("lb://" + loginServiceName))
                .route("CEN-PROJECT-FORECASTING-BE", p -> p
                        .path("/v1/**")
                        .filters(f -> f.filter(jwtRequestFilter)
                                .circuitBreaker(config -> config
                                        .setName("CEN-PROJECT-FORECASTING-BE")
                                        .setFallbackUri("forward:/siForecastFallBackMethod")))
                        .uri("lb://CEN-PROJECT-FORECASTING-BE"))
                .route(commonsServiceName, p -> p
                        .path("/commons/**")
                        .filters(f -> f.filter(jwtRequestFilter)
                                .circuitBreaker(config -> config
                                        .setName(commonsServiceName)
                                        .setFallbackUri("forward:/commonsServiceFallBackMethod"))
                                .rewritePath("/commons/(?<path>.*)", "/${path}"))
                        .uri("lb://" + commonsServiceName))
                .route(documentServiceName, p -> p
                        .path("/docu/**")
                        .filters(f -> f.filter(jwtRequestFilter)
                                .circuitBreaker(config -> config
                                        .setName(documentServiceName)
                                        .setFallbackUri("forward:/documentServiceFallBackMethod"))
                                .rewritePath("/docu/(?<path>.*)", "/${path}"))
                        .uri("lb://" + documentServiceName))
                .route(mpofferServiceName, p -> p
                        .path("/mpo/**")
                        .filters(f -> f.filter(jwtRequestFilter)
                                .circuitBreaker(config -> config
                                        .setName(mpofferServiceName)
                                        .setFallbackUri("forward:/mpofferServiceFallBackMethod"))
                                .rewritePath("/mpo/(?<path>.*)", "/${path}"))
                        .uri("lb://" + mpofferServiceName))
                .route(projectManagementServiceName, p -> p
                        .path("/projectmanagement/**")
                        .filters(f -> f.filter(jwtRequestFilter)
                                .circuitBreaker(config -> config
                                        .setName(projectManagementServiceName)
                                        .setFallbackUri("forward:/projectmanagementServiceFallBackMethod"))
                                .rewritePath("/projectmanagement/(?<path>.*)", "/${path}"))
                        .uri("lb://" + projectManagementServiceName))
                .route("openapi", r -> r
                        .path("/v3/api-docs/**")
                        .filters(f -> f
                                .rewritePath("/v3/api-docs/(?<path>.*)", "/${path}/v3/api-docs"))
                        .uri(swaggerHost))
                .build();
    }
}
