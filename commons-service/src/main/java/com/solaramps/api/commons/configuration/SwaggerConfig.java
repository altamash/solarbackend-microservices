package com.solaramps.api.commons.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Commons application API",
                version = "1.0",
                description = "Commons application API",
                termsOfService = "http://swagger.io/terms/"),
        security = {
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "Bearer Token")
        })
@SecuritySchemes({
        @io.swagger.v3.oas.annotations.security.SecurityScheme(
                name = "Bearer Token",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT")
})
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${swagger.host}") String swaggerHost) {
        Server server = new Server();
        server.setUrl(swaggerHost + "/commons");
        server.setDescription("host");
        return new OpenAPI().servers(List.of(server));
    }

}
