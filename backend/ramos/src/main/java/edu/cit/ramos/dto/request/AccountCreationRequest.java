package edu.cit.ramos.dto.request;

import edu.cit.ramos.entity.SexType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountCreationRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private SexType sex;
    private LocalDate dateOfBirth;
    private String permanentAddress;
    private String currentAddress;
    // Role: accept 'S'/'F'/'A' or full names like STUDENT/FACULTY/STAFF
    private String role;
    // Optional year first enrolled/hired (4-digit). If null, system will use current year.
    private Integer yearFirstEnrolled;
}
