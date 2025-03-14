package net.mausberg.authentication_framework_backend.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTOpublic {

	private Long id;
	private String publicName;
	private LocalDateTime createdAt;
	
	// Constructor that converts AppUser to AppUserDTO
	public AppUserDTOpublic(AppUser appUser) {
		this.id = appUser.getId();
		this.publicName = appUser.getPublicName();
		this.createdAt = appUser.getCreatedAt();
	}

}
