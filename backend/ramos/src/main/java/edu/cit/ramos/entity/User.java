package edu.cit.ramos.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "school_id", nullable = false, unique = true, length = 20)
    private String schoolId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SexType sex;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "permanent_address")
    private String permanentAddress;

    @Column(name = "current_address")
    private String currentAddress;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "password_changed", nullable = false)
    private Boolean passwordChanged = false;

    @Column(name = "date_created", nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @Column(name = "date_updated", nullable = false)
    private OffsetDateTime dateUpdated;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    private AccountStatusType accountStatus;

    @Column(name = "last_login")
    private OffsetDateTime lastLogin;

    // getters and setters
}