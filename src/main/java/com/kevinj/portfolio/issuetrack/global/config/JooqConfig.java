package com.kevinj.portfolio.issuetrack.global.config;

import org.jooq.conf.Settings;
import org.springframework.boot.jooq.autoconfigure.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    @Bean
    public DefaultConfigurationCustomizer jooqConfigurationCustomizer() {
        return configuration -> configuration.set(
            new Settings().withExecuteLogging(false)
        );
    }

}
