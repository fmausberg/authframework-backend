package net.mausberg.authentication_framework_backend.model;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {

	private Long id;
	private String username;
	private String publicName;
	private String firstName;
	private String lastName;
	private LocalDateTime birthday;
	private String mail;
	private String createdAt;
	private LocalDateTime mailVerifiedAt;
	private LocalDateTime verificationTokenCreatedAt;
	private String source;
	private String oAuthProviderID;
	private Set<String> roles;
	
	// Constructor that converts AppUser to AppUserDTO
	public AppUserDTO(AppUser appUser) {
		this.id = appUser.getId();
		this.username = appUser.getUsername();
		this.publicName = appUser.getPublicName();
		this.firstName = appUser.getFirstName();
		this.lastName = appUser.getLastName();
		this.birthday = appUser.getBirthday();
		this.mail = appUser.getMail();
		this.createdAt = appUser.getCreatedAt();
		this.mailVerifiedAt = appUser.getMailVerifiedAt();
		this.verificationTokenCreatedAt = appUser.getVerificationTokenCreatedAt();
		this.source = appUser.getSource();
		this.oAuthProviderID = appUser.getOAuthProviderID();
		this.roles = appUser.getRoles();
	}

}
