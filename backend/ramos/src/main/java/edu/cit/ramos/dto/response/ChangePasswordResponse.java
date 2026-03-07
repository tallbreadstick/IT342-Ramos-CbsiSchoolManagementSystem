package edu.cit.ramos.dto.response;

import lombok.Data;

@Data
public class ChangePasswordResponse {
    private boolean success;
    private String message;
}
