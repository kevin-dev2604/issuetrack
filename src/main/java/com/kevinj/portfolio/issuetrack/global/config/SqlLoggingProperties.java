package com.kevinj.portfolio.issuetrack.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.sql-logging")
public class SqlLoggingProperties {

    /**
     * datasource-proxy 자체 활성화 여부
     */
    private boolean enabled = true;

    /**
     * 일반 SQL 로그 출력 여부
     */
    private boolean logSql = true;

    /**
     * multiline 출력 여부
     */
    private boolean multiline = true;

    /**
     * 슬로우 쿼리 임계치(ms)
     */
    private long slowQueryThresholdMs = 1000L;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isLogSql() {
        return logSql;
    }

    public void setLogSql(boolean logSql) {
        this.logSql = logSql;
    }

    public boolean isMultiline() {
        return multiline;
    }

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    public long getSlowQueryThresholdMs() {
        return slowQueryThresholdMs;
    }

    public void setSlowQueryThresholdMs(long slowQueryThresholdMs) {
        this.slowQueryThresholdMs = slowQueryThresholdMs;
    }
}
