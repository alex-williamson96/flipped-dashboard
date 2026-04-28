package com.flippeddashboard.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class StartupValidator {

    private static final Logger log = LoggerFactory.getLogger(StartupValidator.class);

    private final DataSource dataSource;
    private final String datasourceUrl;
    private final String allowedOrigins;

    public StartupValidator(DataSource dataSource,
                            @Value("${spring.datasource.url:}") String datasourceUrl,
                            @Value("${cors.allowed-origins:}") String allowedOrigins) {
        this.dataSource = dataSource;
        this.datasourceUrl = datasourceUrl;
        this.allowedOrigins = allowedOrigins;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void validateOnStartup() {
        log.info("Startup check: datasource URL host = {}", maskJdbcUrl(datasourceUrl));
        log.info("Startup check: CORS allowed origins = {}", allowedOrigins.isBlank() ? "(default)" : allowedOrigins);

        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                log.info("Startup check: database connection OK ({} {})",
                        connection.getMetaData().getDatabaseProductName(),
                        connection.getMetaData().getDatabaseProductVersion());
            } else {
                log.error("Startup check: database connection is not valid");
            }
        } catch (SQLException e) {
            log.error("Startup check: database connection failed - {}", e.getMessage());
        }
    }

    private static String maskJdbcUrl(String url) {
        if (url == null || url.isBlank()) return "(unset)";
        int queryStart = url.indexOf('?');
        return queryStart >= 0 ? url.substring(0, queryStart) : url;
    }
}
