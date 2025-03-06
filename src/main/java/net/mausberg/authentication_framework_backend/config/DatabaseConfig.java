package net.mausberg.authentication_framework_backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${DB_URL}")
    private String dbUrl;

    @Value("${DB_USERNAME}")
    private String dbUsername;

    @Value("${DB_PASSWORD}")
    private String dbPassword;

    private boolean isDatabaseReachable = true;

    @Bean
    @Primary
    @Conditional(DatabaseReachableCondition.class)
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        try (Connection connection = dataSource.getConnection()) {
            // Test the connection by executing a simple query
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery("SELECT 1");
            }
        } catch (SQLException e) {
            isDatabaseReachable = false;
            logger.error("Database connection failed: " + e.getMessage());
        }

        return dataSource;
    }

    public boolean isDatabaseReachable() {
        return isDatabaseReachable;
    }
}