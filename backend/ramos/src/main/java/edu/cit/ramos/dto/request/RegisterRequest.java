package edu.cit.ramos.dto.request;

import edu.cit.ramos.entity.SexType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
	private String schoolId;
	private String email;
	private String firstName;
	private String lastName;
	private String middleName;
	private SexType sex;
	private LocalDate dateOfBirth;
	private String permanentAddress;
	private String currentAddress;
}
