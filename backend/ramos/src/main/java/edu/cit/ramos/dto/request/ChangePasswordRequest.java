package edu.cit.ramos.dto.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
}
