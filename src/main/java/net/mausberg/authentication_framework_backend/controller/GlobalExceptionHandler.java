package net.mausberg.authentication_framework_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import net.mausberg.authentication_framework_backend.config.JwtTokenExpiredException;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		String errorMessage = "Error: " + ex.getMessage();
		logger.error(errorMessage);
		return new ResponseEntity<>(
				new ErrorResponse(errorMessage),
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<ErrorResponse> handleMessagingException(MessagingException e) {
		String errorMessage = "Email sending error: " + e.getMessage();
		logger.error(errorMessage);
		return new ResponseEntity<>(
				new ErrorResponse(errorMessage),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(JwtTokenExpiredException.class)
	public ResponseEntity<ErrorResponse> handleJwtTokenExpiredException(ExpiredJwtException ex) {
		String errorMessage = "JWT token has expired. Please login again.(caught with Spring MVC Exception)";
		logger.error(errorMessage);
		return new ResponseEntity<>(
				new ErrorResponse(errorMessage),
				HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
		String errorMessage = "JWT token has expired. Please login again. (caught with JWT Exception)";
		logger.error(errorMessage);
		return new ResponseEntity<>(
				new ErrorResponse(errorMessage),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
		String errorMessage = "An unexpected error occurred: " + ex.getMessage();
		logger.error(errorMessage);
		return new ResponseEntity<>(
				new ErrorResponse(errorMessage),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
