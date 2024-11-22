package net.mausberg.authentication_framework_backend.repository;

import net.mausberg.authentication_framework_backend.model.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	
	AppUser findByUsername(String username);

	AppUser findByMail(String mail);

	AppUser findByVerificationToken(String token);
}
