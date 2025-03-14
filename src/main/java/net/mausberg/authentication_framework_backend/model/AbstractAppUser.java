package net.mausberg.authentication_framework_backend.model;

import java.time.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class AbstractAppUser implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	private String publicName; //changeable
	private String firstName; //changeable
	private String lastName; //changeable
	private LocalDate birthday; 
	private String password;
	private String mail;
	private String phone;
	private String createdAt;
	private LocalDateTime mailVerifiedAt;
	private String verificationToken;
	private LocalDateTime verificationTokenCreatedAt;
	private String source;
	private String oAuthProviderID;
	private String passwordResetToken;
	private LocalDateTime passwordResetTokenCreatedAt;
	
	@ElementCollection(fetch = FetchType.EAGER)  // Store roles in a Set
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	private Set<String> roles;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}
}
