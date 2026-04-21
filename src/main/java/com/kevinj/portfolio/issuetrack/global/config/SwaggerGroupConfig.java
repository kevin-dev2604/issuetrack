package com.kevinj.portfolio.issuetrack.global.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerGroupConfig {

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
            .group("1-all")
            .pathsToMatch("/**")
            .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
            .group("2-user")
            .pathsToMatch("/process/**", "/issue/**", "/dilemma/user/*")
            .build();
    }

    @Bean
    public GroupedOpenApi dilemmaApi() {
        return GroupedOpenApi.builder()
            .group("3-dilemma")
            .pathsToMatch("/dilemma/*/close")
            .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
            .group("4-admin")
            .pathsToMatch("/admin/**")
            .build();
    }
}
