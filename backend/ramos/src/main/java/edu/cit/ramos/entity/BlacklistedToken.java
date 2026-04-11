package edu.cit.ramos.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "blacklisted_tokens", uniqueConstraints = {@UniqueConstraint(columnNames = {"token"})})
public class BlacklistedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, length = 500, unique = true)
    private String token;

    @Column(name = "blacklisted_at", nullable = false)
    private OffsetDateTime blacklistedAt;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;
}
