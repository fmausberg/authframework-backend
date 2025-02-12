package net.mausberg.authentication_framework_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import net.mausberg.authentication_framework_backend.model.AppUser;
import net.mausberg.authentication_framework_backend.model.AppUserDTO;
import net.mausberg.authentication_framework_backend.service.AppUserService;

@RestController
@RequestMapping("/api/v0/appuser")
public class AppUserController {
   
	private final AppUserService appUserService;

	@Autowired
	public AppUserController(AppUserService appUserService) {
		this.appUserService = appUserService;
	}

	@GetMapping("/count")
	public long getAppUserCount() {
		return appUserService.getAppUserCount();
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createAppUser(@RequestBody AppUser appUserRequest, @RequestParam boolean sendMail, Authentication authentication) {
		try {
			AppUser principal = appUserService.getAppUserByMail(authentication.getName());
			if (!appUserService.canCreateAppUser(authentication)) {
				return new ResponseEntity<>(new ErrorResponse("Access denied: AppUser is not allowed to creade new AppUsers"), HttpStatus.FORBIDDEN);
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
	
	@PostMapping("/updateme")
	public ResponseEntity<?> updateAppUserDTO(@RequestBody AppUserDTO appUserDTO, Authentication authentication) throws Exception{
	    AppUser principal = appUserService.getAppUserByMail(authentication.getName());
	    
	    AppUser updatedUser = appUserService.updateUserAttributes(principal, appUserDTO);
		
		return ResponseEntity.ok(new AppUserDTO(updatedUser));	
	}
	
	@GetMapping("/me")
	public AppUserDTO getMyAppuserData() {
		return new AppUserDTO(appUserService.getAppUserByMail(SecurityContextHolder.getContext().getAuthentication().getName()));
	}
	
}
