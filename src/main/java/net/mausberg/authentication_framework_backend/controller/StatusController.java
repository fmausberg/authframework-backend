package net.mausberg.authentication_framework_backend.controller;

import net.mausberg.authentication_framework_backend.config.DatabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @Autowired
    private DatabaseConfig databaseConfig;

    @GetMapping("/status")
    public String getStatus() {
        if (databaseConfig.isDatabaseReachable()) {
            return "Database is reachable";
        } else {
            return "Database is not reachable";
        }
    }
}
