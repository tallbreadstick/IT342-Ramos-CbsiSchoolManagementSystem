package edu.cit.ramos.user_records.dto.response;

import lombok.Data;

@Data
public class AccountCreationResponse {
    private Long id;
    private String email;
    private String schoolId;
    private String generatedPassword;
    private boolean success;
    private String message;
}
