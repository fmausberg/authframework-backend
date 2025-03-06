package net.mausberg.authentication_framework_backend.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseReachableCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String dbUrl = context.getEnvironment().getProperty("DB_URL");
        String dbUsername = context.getEnvironment().getProperty("DB_USERNAME");
        String dbPassword = context.getEnvironment().getProperty("DB_PASSWORD");

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}