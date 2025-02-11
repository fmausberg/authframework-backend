//AuthenticationFrameworkBackendApplication.java
package net.mausberg.authentication_framework_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication(scanBasePackages = "net.mausberg.authentication_framework_backend")
@EnableJpaRepositories(basePackages = "net.mausberg.authentication_framework_backend.repository")
@EntityScan(basePackages = "net.mausberg.authentication_framework_backend.model")
public class AuthenticationFrameworkBackendApplication {

	public static void main(String[] args) {

		//load environment variables for mvn packaging and spring-boot start
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(AuthenticationFrameworkBackendApplication.class, args);
	}

}
