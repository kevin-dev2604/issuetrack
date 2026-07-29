package com.kevinj.portfolio.issuetrack.global.config;

import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(SqlLoggingProperties.class)
public class DataSourceProxyConfig {

    @Bean
    public BeanPostProcessor dataSourceProxyBeanPostProcessor(SqlLoggingProperties properties) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (!properties.isEnabled()) {
                    return bean;
                }

                if (!(bean instanceof DataSource dataSource)) {
                    return bean;
                }

                if (bean instanceof ProxyDataSource) {
                    return bean;
                }

                ProxyDataSourceBuilder builder = ProxyDataSourceBuilder
                    .create(dataSource)
                    .name(beanName)
                    .listener(new AppSqlLoggingListener(properties));

                if (properties.isMultiline()) {
                    builder.multiline();
                }

                return builder.build();
            }
        };
    }

    @Slf4j(topic = "com.kevinj.potfolio.common.logging.sql")
    static class AppSqlLoggingListener implements QueryExecutionListener {

        private final SqlLoggingProperties properties;

        AppSqlLoggingListener(SqlLoggingProperties properties) {
            this.properties = properties;
        }

        @Override
        public void beforeQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
            // no-op
        }

        @Override
        public void afterQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
            String sql = queryInfoList.stream()
                .map(QueryInfo::getQuery)
                .collect(Collectors.joining(";\n"));

            long elapsed = execInfo.getElapsedTime();
            boolean success = execInfo.isSuccess();

            if (elapsed >= properties.getSlowQueryThresholdMs()) {
                log.warn("""
                        
                        [SLOW-QUERY]
                        time={} ms
                        success={}
                        type={}
                        batch={}
                        queries=
                        {}
                        """,
                    elapsed,
                    success,
                    execInfo.getStatementType(),
                    execInfo.isBatch(),
                    sql
                );
                return;
            }

            if (properties.isLogSql() && log.isDebugEnabled()) {
                log.debug("""
                        
                        [SQL]
                        time={} ms
                        success={}
                        type={}
                        batch={}
                        queries=
                        {}
                        """,
                    elapsed,
                    success,
                    execInfo.getStatementType(),
                    execInfo.isBatch(),
                    sql
                );
            }
        }
    }
}
