package net.mausberg.authentication_framework_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public boolean isDatabaseAvailable() {
		try {
			// Execute a simple query to check if the database is available
			jdbcTemplate.queryForObject("SELECT 1", Integer.class);
			return true; // If no exception is thrown, the database is available
		} catch (Exception e) {
			return false; // If an exception is thrown, the database is not available
		}
	}
}
