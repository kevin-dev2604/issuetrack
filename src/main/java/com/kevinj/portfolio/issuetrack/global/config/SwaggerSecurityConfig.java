package com.kevinj.portfolio.issuetrack.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerSecurityConfig {

    @Bean
    public OpenAPI openAPI() {
        final String schemeName = "bearerAuth";
        return new OpenAPI()
            .info(new Info()
                .title("IssueTrack API")
                .version("v1")
                .description("Issue tracker API document for portfolio")
                .contact(new Contact()
                    .name("kevin.j")
                    .email("kevin.j.dev.2604@gmail.com")))
            .addSecurityItem(new SecurityRequirement().addList(schemeName))
            .components(new Components()
                .addSecuritySchemes(schemeName, new SecurityScheme()
                    .name(schemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                )
            );
    }
}
