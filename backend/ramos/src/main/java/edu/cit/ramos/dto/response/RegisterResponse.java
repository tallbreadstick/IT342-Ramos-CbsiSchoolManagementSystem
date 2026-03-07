package edu.cit.ramos.dto.response;

import lombok.Data;

@Data
public class RegisterResponse {
	private Long id;
	private String email;
	private String generatedPassword;
	private String schoolId;
	private String message;
}
