package edu.cit.ramos.dto.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String schoolId;
    private String oldPassword;
    private String newPassword;
}
