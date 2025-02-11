package net.mausberg.authentication_framework_backend;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = "net.mausberg.authentication_framework_backend")
@EnableJpaRepositories(basePackages = "net.mausberg.authentication_framework_backend.repository")
@EntityScan(basePackages = "net.mausberg.authentication_framework_backend.model")
public class AuthenticationFrameworkBackendApplication {

	@Autowired
	private Dotenv dotenv;
	
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        System.out.println("MAIL_USERNAME: " + System.getProperty("MAIL_USERNAME"));
        System.out.println("MAIL_PASSWORD: " + System.getProperty("MAIL_PASSWORD"));
        System.out.println("JWT_SECRET: " + System.getProperty("JWT_SECRET"));

		SpringApplication.run(AuthenticationFrameworkBackendApplication.class, args);
	}

	@PostConstruct
    public void init() {
        System.out.println("JWT_SECRET: " + dotenv.get("JWT_SECRET"));
    }

}
