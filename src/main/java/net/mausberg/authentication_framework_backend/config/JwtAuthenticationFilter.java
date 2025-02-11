package net.mausberg.authentication_framework_backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.lang.NonNull;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	private final UserDetailsService userDetailsService;

	@Autowired
	public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain chain)
			throws ServletException, IOException {

		String path = request.getRequestURI();

		// Skip JWT validation for specific endpoints
		if (
				path.equals("/login")
				|| path.equals("/api/v0/test/public")
				|| path.equals("/api/v0/test/check")
				|| path.equals("/api/v0/auth/register")
				|| path.equals("/api/v0/auth/verifyMail")
				|| path.equals("/api/v0/auth/directlogin")
				|| path.equals("/api/v0/auth/requestnewpassword")
				|| path.equals("/api/v0/auth/loginWithPasswordResetToken")
			){
			logger.debug("JwtAuthenticationFilter.doFilterInternal().path = " + path + " doesn't need JWT");
			chain.doFilter(request, response);
			return;
		}
		logger.debug("JwtAuthenticationFilter.doFilterInternal().path = " + path + " will be checked for JWT");

		String header = request.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			try {
				String username = jwtUtil.getMailFromToken(token);
	
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	
					if (jwtUtil.validateToken(token)) {
						UsernamePasswordAuthenticationToken authToken =
								new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}
			} catch (ExpiredJwtException e) {
				System.out.println("\tFPM: Expired JwtException");
				logger.warn("JWT has expired: {}", e);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("application/json");
				response.getWriter().write("{\"error\": \"JWT token has expired. Please login again.\"}");
				return; // Exit the filter without proceeding further
			}
		}
		chain.doFilter(request, response);
	}
}
