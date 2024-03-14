package com.financing.app.monitoring;

import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.util.Map;

@Component
public class DatabaseInfoContributor implements InfoContributor {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseInfoContributor(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void contribute(Builder builder) {
        jdbcTemplate.execute((ConnectionCallback<Object>) (connection) -> {
            DatabaseMetaData metaData = connection.getMetaData();
            builder.withDetail("database", Map.of(
                    "url", metaData.getURL(),
                    "databaseProductName", metaData.getDatabaseProductName(),
                    "databaseProductVersion", metaData.getDatabaseProductVersion(),
                    "driverName", metaData.getDriverName(),
                    "driverVersion", metaData.getDriverVersion()
            ));
            return null;
        });
    }
}

