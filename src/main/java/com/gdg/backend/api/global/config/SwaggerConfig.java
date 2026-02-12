package com.gdg.backend.api.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "triples API", version = "v1"),
        security = @SecurityRequirement(name = "BearerAuth")
)
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi oauthApi() {
        return GroupedOpenApi.builder()
                .group("Oauth API")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("Auth API")
                .pathsToMatch("/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("User API")
                .pathsToMatch("/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi recordApi() {
        return GroupedOpenApi.builder()
                .group("Record API")
                .pathsToMatch("/record/**")
                .build();
    }

    @Bean
    public GroupedOpenApi mindMapApi() {
        return GroupedOpenApi.builder()
                .group("MindMap API")
                .pathsToMatch("/mindmap/**")
                .build();
    }

    @Bean
    public GroupedOpenApi dashboardApi(){
        return GroupedOpenApi.builder()
                .group("Dashboard API")
                .pathsToMatch("/dashboard/**")
                .build();
    }

    @Bean
    public GroupedOpenApi myPageApi(){
        return GroupedOpenApi.builder()
                .group("MyPage API")
                .pathsToMatch("/mypage/**")
                .build();
    }

    @Bean
    public GroupedOpenApi gole(){
        return GroupedOpenApi.builder()
                .group("gole API")
                .pathsToMatch("/goles/**")
                .build();
    }

}
