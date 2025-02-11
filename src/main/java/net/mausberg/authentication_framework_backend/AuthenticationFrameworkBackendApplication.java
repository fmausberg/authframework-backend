//AuthenticationFrameworkBackendApplication.java
package net.mausberg.authentication_framework_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.github.cdimascio.dotenv.Dotenv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication(scanBasePackages = "net.mausberg.authentication_framework_backend")
@EnableJpaRepositories(basePackages = "net.mausberg.authentication_framework_backend.repository")
@EntityScan(basePackages = "net.mausberg.authentication_framework_backend.model")
public class AuthenticationFrameworkBackendApplication {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFrameworkBackendApplication.class);
	
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		dotenv.entries().forEach(entry -> logger.info("Loaded env: {}={}", entry.getKey(), entry.getValue()));
		logger.info("MAIL_USERNAME: {}", System.getProperty("MAIL_USERNAME"));
        logger.info("MAIL_PASSWORD: {}", System.getProperty("MAIL_PASSWORD"));
        logger.info("JWT_SECRET: {}", System.getProperty("JWT_SECRET"));
		SpringApplication.run(AuthenticationFrameworkBackendApplication.class, args);
	}

}
