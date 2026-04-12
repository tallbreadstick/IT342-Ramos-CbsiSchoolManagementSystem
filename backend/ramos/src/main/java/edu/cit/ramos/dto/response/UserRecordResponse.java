package edu.cit.ramos.dto.response;

import edu.cit.ramos.entity.AccountStatusType;
import edu.cit.ramos.entity.SexType;
import lombok.Data;

import java.time.OffsetDateTime;
import java.time.LocalDate;

@Data
public class UserRecordResponse {
    private Long id;
    private String schoolId;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private SexType sex;
    private LocalDate dateOfBirth;
    private String permanentAddress;
    private String currentAddress;
    private OffsetDateTime dateCreated;
    private OffsetDateTime dateUpdated;
    private AccountStatusType accountStatus;
    private OffsetDateTime lastLogin;
}
