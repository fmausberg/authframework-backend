package net.mausberg.authentication_framework_backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import net.mausberg.authentication_framework_backend.service.DatabaseService;

/**
 * TestController is a REST controller that provides endpoints for testing the backend and database connectivity.
 */
@RestController
@RequestMapping("/api/v0/test")
@Tag(name = "Test Controller", description = "Endpoints for testing backend and database connectivity")
    @SecuritySchemes({
        @SecurityScheme(
            name = "bearerAuth",
            type = SecuritySchemeType.HTTP,
            scheme = "bearer",
            bearerFormat = "JWT"
        )
    })
    @SecurityRequirement(name = "bearerAuth")
public class TestController {

    @Autowired
    private DatabaseService databaseService;

    /**
     * Endpoint to check the availability of the backend and the database.
     *
     * @return a map containing the status of the backend and the database.
     */
    @Operation(
        summary = "Check the availability of the backend and the database",
        description = "Returns the availability status of the backend and the database."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved status"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/check")
    public Map<String, String> checkConnection(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        Map<String, String> status = new HashMap<>();

        status.put("backend", "available");

        // Check database availability
        String databaseStatus = databaseService.isDatabaseAvailable() ? "available" : "not available";
        status.put("database", databaseStatus);
        status.put("origin", origin);
        status.put("Check", "C1");
        

        return status;
    }

    /**
     * Endpoint to get public information.
     *
     * @return a string containing public information.
     */
    @Operation(
        summary = "Get public information",
        description = "Returns a static public information string."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved public information"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/public")
    public @ResponseBody String getPublicInformation() {
        return "public Information";
    }
}
