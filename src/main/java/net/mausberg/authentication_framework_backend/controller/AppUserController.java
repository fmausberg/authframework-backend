package net.mausberg.authentication_framework_backend.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import net.mausberg.authentication_framework_backend.model.*;
import net.mausberg.authentication_framework_backend.service.AppUserService;

/**
 * AppUserController is a REST controller that provides endpoints for managing app users.
 */
@RestController
@RequestMapping("/api/v0/appuser")
public class AppUserController {
   
    private final AppUserService appUserService;

    /**
     * Constructs an AppUserController with the specified AppUserService.
     * 
     * @param appUserService the service to manage app users
     */
    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     * Retrieves the total count of app users.
     * 
     * @return the total count of app users
     */
    @GetMapping("/count")
    public long getAppUserCount() {
        return appUserService.getAppUserCount();
    }
    
    /**
     * Creates a new app user.
     * 
     * @param appUserRequest the app user details
     * @param sendMail whether to send a registration email
     * @param authentication the authentication object containing the user's details
     * @return a response entity containing the created app user
     */
    @PostMapping("/create")
    public ResponseEntity<?> createAppUser(@RequestBody AppUser appUserRequest, @RequestParam boolean sendMail, Authentication authentication) {
        try {
            AppUser principal = appUserService.getAppUserByMail(authentication.getName());
            if (!appUserService.canCreateAppUser(authentication)) {
                return new ResponseEntity<>(new ErrorResponse("Access denied: AppUser is not allowed to create new AppUsers"), HttpStatus.FORBIDDEN);
            }
            AppUser appUser = appUserService.createAppUser(appUserRequest.getMail(), appUserRequest.getPassword(), principal, sendMail, null, null, "Indirect");
            return new ResponseEntity<>(new AppUserDTO(appUser), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Detailed exception handling
            String errorMessage = "Error: " + e.getMessage();
            return new ResponseEntity<>(new ErrorResponse(errorMessage), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Generic catch for any other unhandled exceptions
            String errorMessage = "An unexpected error occurred: " + e.getMessage();
            return new ResponseEntity<>(new ErrorResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Updates the authenticated user's details.
     * 
     * @param appUserDTO the updated app user details
     * @param authentication the authentication object containing the user's details
     * @return a response entity containing the updated app user
     * @throws Exception if an error occurs while updating the user
     */
    @PostMapping("/updateme")
    public ResponseEntity<?> updateMeAppUserDTO(@RequestBody AppUserDTO appUserDTO, Authentication authentication) throws Exception {
        AppUser principal = appUserService.getAppUserByMail(authentication.getName());
        AppUser updatedUser = appUserService.updateUserAttributes(principal, appUserDTO);
        return ResponseEntity.ok(new AppUserDTO(updatedUser));	
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateAppUserDTO(@RequestBody AppUserDTO appUserDTO, Authentication authentication) throws Exception {
        AppUser principal = appUserService.getAppUserByMail(authentication.getName());
        for (String attribute : Set.of("publicName", "firstName", "lastName", "birthday", "phone")) {
            if (appUserDTO.getClass().getMethod("get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1)).invoke(appUserDTO) != null) {
                if (!appUserService.hasPermissionToUpdate(principal, attribute)) {
                    return new ResponseEntity<>(new ErrorResponse("Access denied: You do not have permission to update " + attribute), HttpStatus.FORBIDDEN);
                }
            }
        }
        AppUser updatedUser = appUserService.updateUserAttributes(principal, appUserDTO);
        return ResponseEntity.ok(new AppUserDTO(updatedUser));	
    }
    
    /**
     * Retrieves the authenticated user's details.
     * 
     * @return the authenticated user's details
     */
    @GetMapping("/me")
    public AppUserDTO getMyAppuserData() {
        return new AppUserDTO(appUserService.getAppUserByMail(SecurityContextHolder.getContext().getAuthentication().getName()));
    }
    
}
