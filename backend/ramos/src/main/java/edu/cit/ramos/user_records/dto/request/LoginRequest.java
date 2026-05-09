package edu.cit.ramos.user_records.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
	private String schoolId;
	private String password;
}
