package net.mausberg.authentication_framework_backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import net.mausberg.authentication_framework_backend.model.AppUser;
import net.mausberg.authentication_framework_backend.model.AppUserDTO;
import net.mausberg.authentication_framework_backend.service.AppUserService;

@RestController
@RequestMapping("/api/v0/auth/")
public class AuthController {
	
	private final AppUserService appUserService;

	@Autowired
	public AuthController(AppUserService appUserService) {
		this.appUserService = appUserService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerAppUser(@RequestBody AppUser appUserRequest) throws MessagingException {
			AppUser appUser = appUserService.createAppUser(
					appUserRequest.getMail(),		// mail
					appUserRequest.getPassword(),	// password
					null,							// creator
					true,							// sendMail
					appUserRequest.getFirstName(),	// firstName
					appUserRequest.getLastName(),	// lastName
					"Direct");						// source
			return ResponseEntity.status(201).body(new AppUserDTO(appUser)); 
	}
	
	@PostMapping("/verifyMail")
	public ResponseEntity<?> verifyVerificationToken(@RequestBody String verificationToken){

		AppUser appUser = appUserService.verifyMailByToken(verificationToken);
		Map<String, Object> response = new HashMap<>();
		response.put("jwttoken", appUserService.generateToken(appUser));
		response.put("appUser", new AppUserDTO(appUser));			
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/directlogin")
	public ResponseEntity<?> directLogin(@RequestBody Map<String, String> credentials) {
			AppUser appUser = appUserService.authenticate(credentials);

			if (appUser != null) {
				// Generate a token (e.g., JWT) and return it
				Map<String, Object> response = new HashMap<>();
				response.put("jwttoken", appUserService.generateToken(appUser));
				response.put("appUser", new AppUserDTO(appUser));
				return ResponseEntity.ok(response);
			} else {
				// Return Unauthorized if authentication fails
				return new ResponseEntity<>(new ErrorResponse("Invalid email or password"), HttpStatus.UNAUTHORIZED);
			}
	}
	
	@PostMapping("/requestnewpassword")
	public ResponseEntity<?> requestNewPassword(@RequestBody String mail) throws MessagingException {
		appUserService.sendNewPasswordVerificationLink(mail);
		return ResponseEntity.ok(Map.of("message", "Password reset instructions have been sent."));
	}

}
