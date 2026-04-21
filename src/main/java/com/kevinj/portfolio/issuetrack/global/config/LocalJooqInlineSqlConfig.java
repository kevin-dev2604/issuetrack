package com.kevinj.portfolio.issuetrack.global.config;

import lombok.extern.slf4j.Slf4j;
import org.jooq.ExecuteContext;
import org.jooq.Query;
import org.jooq.conf.ParamType;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.jooq.autoconfigure.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class LocalJooqInlineSqlConfig {

    @Bean
    public DefaultConfigurationCustomizer localJooqInlineSqlCustomizer() {
        return configuration -> configuration.set(
            new DefaultExecuteListenerProvider(new LocalJooqInlineSqlListener())
        );
    }

    @Slf4j(topic = "com.kevinj.potfolio.common.logging.jooq-inline")
    static class LocalJooqInlineSqlListener extends DefaultExecuteListener {

        @Override
        public void renderEnd(ExecuteContext ctx) {
            Query query = ctx.query();
            if (query == null || !log.isDebugEnabled()) {
                return;
            }

            String inlinedSql = query.getSQL(ParamType.INLINED);

            log.debug("""
                    
                    [JOOQ-INLINED]
                    {}
                    """, inlinedSql);
        }
    }
}
