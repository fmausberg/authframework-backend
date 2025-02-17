package net.mausberg.authentication_framework_backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mausberg.authentication_framework_backend.service.DatabaseService;

/**
 * TestController is a REST controller that provides endpoints for testing the backend and database connectivity.
 */
@RestController
@RequestMapping("/api/v0/test")
public class TestController {
    
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    
    @Autowired
    private DatabaseService databaseService;

    /**
     * Endpoint to check the availability of the backend and the database.
     * 
     * @return a map containing the status of the backend and the database.
     */
    @GetMapping("/check")
    public Map<String, String> checkConnection() {
        logger.debug("\tFPM: TestController.checkConnection() started");
        Map<String, String> status = new HashMap<>();
        
        status.put("backend", "available");
        
        // Check database availability
        String databaseStatus = databaseService.isDatabaseAvailable() ? "available" : "not available";
        status.put("database", databaseStatus);

        return status;
    }
    
    /**
     * Endpoint to get public information.
     * 
     * @return a string containing public information.
     */
    @GetMapping("/public")
    public @ResponseBody String getPublicInformation() {
        return "public Information";
    }
}
