package edu.cit.ramos.user_records.dto.response;

import lombok.Data;

@Data
public class ChangePasswordResponse {
    private boolean success;
    private String message;
}
