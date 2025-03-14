package net.mausberg.authentication_framework_backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import net.mausberg.authentication_framework_backend.model.*;
import net.mausberg.authentication_framework_backend.service.AppUserService;

/**
 * AuthController is a REST controller that provides endpoints for user authentication and registration.
 */
@RestController
@RequestMapping("/api/v0/auth/")
public class AuthController {
    
    private final AppUserService appUserService;

    /**
     * Constructs an AuthController with the specified AppUserService.
     * 
     * @param appUserService the service to manage app users
     */
    @Autowired
    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }
    
    /**
     * Registers a new app user.
     * 
     * @param appUserRequest the app user details
     * @return a response entity containing the created app user
     * @throws MessagingException if an error occurs while sending the registration email
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerAppUser(@RequestBody AppUser appUserRequest) throws MessagingException {
        appUserRequest.setSource("Direct");
        AppUser appUser = appUserService.createAppUser(appUserRequest, true);
        return ResponseEntity.status(201).body(new AppUserDTO(appUser)); 
    }

    /**
     * Resends the verification email to the user.
     * 
     * @param request a map containing the username
     * @param authentication the authentication object containing the user's details
     * @return a response entity containing a success message
     * @throws MessagingException if an error occurs while sending the verification email
     */
    @PostMapping("/sendVerifcationMail")
    public ResponseEntity<?> resendVerificationMail(@RequestBody Map<String, String> request, Authentication authentication) throws MessagingException {
        String username = request.get("username");
        AppUser appUser = appUserService.getAppUserByUsername(username);
        if (appUser == null) {
            return new ResponseEntity<>(new ErrorResponse("User not found"), HttpStatus.NOT_FOUND);
        }
        appUserService.resendVerificationMail(appUser);
        return ResponseEntity.ok(Map.of("message", "Verification mail has been sent."));
    }
    
    /**
     * Verifies the user's email using the verification token.
     * 
     * @param verificationToken the verification token
     * @return a response entity containing the user's first name
     */
    @PostMapping("/verifyMail")
    public ResponseEntity<?> verifyVerificationToken(@RequestBody String verificationToken){
        AppUser appUser = appUserService.verifyMailByToken(verificationToken);
        Map<String, Object> response = new HashMap<>();
        response.put("firstName", appUser.getFirstName());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Authenticates the user using email and password.
     * 
     * @param credentials a map containing the email and password
     * @return a response entity containing the JWT token and app user details
     */
    @PostMapping("/directlogin")
    public ResponseEntity<?> directLogin(@RequestBody Map<String, String> credentials) {
        AppUser appUser = appUserService.authenticate(credentials);

        if (appUser != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("jwttoken", appUserService.generateToken(appUser));
            response.put("appUser", new AppUserDTO(appUser));
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>(new ErrorResponse("Invalid email or password"), HttpStatus.UNAUTHORIZED);
        }
    }
    
    /**
     * Authenticates the user using a password reset token.
     * 
     * @param passwordResetToken the password reset token
     * @return a response entity containing the JWT token and app user details
     */
    @PostMapping("/loginWithPasswordResetToken")
    public ResponseEntity<?> loginWithPWResetToken(@RequestBody String passwordResetToken) {
        passwordResetToken = passwordResetToken.trim().replaceAll("^\"|\"$", "");
        AppUser appUser = appUserService.authenticateByPasswordResetToken(passwordResetToken);
        if (appUser != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("jwttoken", appUserService.generateToken(appUser));
            response.put("appUser", new AppUserDTO(appUser));
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>(new ErrorResponse("Invalid email or password"), HttpStatus.UNAUTHORIZED);
        }
    }
    
    /**
     * Requests a new password reset link to be sent to the user's email.
     * 
     * @param mail the user's email
     * @return a response entity containing a success message
     * @throws MessagingException if an error occurs while sending the password reset email
     */
    @PostMapping("/requestnewpassword")
    public ResponseEntity<?> requestNewPassword(@RequestBody String mail) throws MessagingException {
        mail = mail.trim().replaceAll("^\"|\"$", "");
        appUserService.sendPasswordResetLink(mail);
        return ResponseEntity.ok(Map.of("message", "Password reset instructions have been sent."));
    }
    
    /**
     * Resets the user's password.
     * 
     * @param newPassword the new password
     * @param authentication the authentication object containing the user's details
     * @return a response entity containing the JWT token and app user details
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody String newPassword, Authentication authentication) {
        AppUser principal = appUserService.getAppUserByMail(authentication.getName());
        principal = appUserService.resetPassword(principal, newPassword);
        Map<String, Object> response = new HashMap<>();
        response.put("jwttoken", appUserService.generateToken(principal));
        response.put("appUser", new AppUserDTO(principal));
        return ResponseEntity.ok(response);
    }

    /**
     * A test endpoint to check if the controller is working.
     * 
     * @return a greeting message
     */
    @GetMapping("/test")
    public String test() {
        return "Test successfull! Entpoint available";
    }
}

