package edu.cit.ramos.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
	private String schoolId;
	private String password;
}
