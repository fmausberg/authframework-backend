package net.mausberg.authentication_framework_backend.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTOlimited {

	private Long id;
	private String username;
	private String publicName;
	private String firstName;
	private String lastName;
	private LocalDate birthday;
	private String mail;
	private String phone;
	private LocalDateTime createdAt;
	
	// Constructor that converts AppUser to AppUserDTOlimited
	public AppUserDTOlimited(AppUser appUser) {
		this.id = appUser.getId();
		this.username = appUser.getUsername();
		this.publicName = appUser.getPublicName();
		this.firstName = appUser.getFirstName();
		this.lastName = appUser.getLastName();
		this.birthday = appUser.getBirthday();
		this.mail = appUser.getMail();
		this.phone = appUser.getPhone();
		this.createdAt = appUser.getCreatedAt();
	}

}
