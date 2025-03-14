package net.mausberg.authentication_framework_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtFilter;

	@Autowired
	public SecurityConfig(@Lazy JwtAuthenticationFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Apply custom CORS configuration
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers("/api/v0/test/check").permitAll()
				.requestMatchers("/api/v0/test/public").permitAll()
				.requestMatchers("/api/v0/auth/register").permitAll()
				.requestMatchers("/api/v0/auth/verifyMail").permitAll()
				.requestMatchers("/api/v0/auth/directlogin").permitAll()
				.requestMatchers("/api/v0/auth/requestnewpassword").permitAll()
				.requestMatchers("/api/v0/auth/loginWithPasswordResetToken").permitAll()
				.requestMatchers("/favicon.ico").permitAll()
				.requestMatchers("/swagger-ui/**").permitAll()
				.requestMatchers("/v3/api-docs/**").permitAll()
				.anyRequest().authenticated())
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.logout(logout -> logout.permitAll());

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// CORS configuration for HTTP security
	public UrlBasedCorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		
		config.addAllowedOrigin("*");  // Allow frontend origin
		config.addAllowedMethod("*");  // Allow all methods (GET, POST, etc.)
		config.addAllowedHeader("*");  // Allow all headers
		config.setAllowCredentials(true);  // Allow credentials (cookies, authorization headers)
		source.registerCorsConfiguration("/**", config);
	  
		return source;
	}
}

