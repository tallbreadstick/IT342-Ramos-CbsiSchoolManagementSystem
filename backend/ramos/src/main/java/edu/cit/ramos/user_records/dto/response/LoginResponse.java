package edu.cit.ramos.user_records.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
	private boolean success;
	private String message;
	private boolean mustChangePassword;
	private String token;
}
