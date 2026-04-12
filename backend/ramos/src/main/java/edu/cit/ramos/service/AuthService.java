package edu.cit.ramos.service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import edu.cit.ramos.dto.request.ChangePasswordRequest;
import edu.cit.ramos.dto.request.LoginRequest;
import edu.cit.ramos.dto.response.ChangePasswordResponse;
import edu.cit.ramos.dto.response.LoginResponse;
import edu.cit.ramos.entity.AccountStatusType;
import edu.cit.ramos.entity.User;
import edu.cit.ramos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class AuthService {

	private final UserRepository userRepository;

	private final Argon2 argon2;
	private byte[] jwtSecretBytes;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

	@Value("${jwt.expiration-ms:86400000}")
	private long jwtExpirationMs;

	@Value("${jwt.secret:change_me}")
	private String jwtSecret;

	@Autowired
	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.argon2 = Argon2Factory.create();
	}

	@PostConstruct
	private void init() {
		// Convert secret to bytes; if using env var ensure it's a strong random value
		this.jwtSecretBytes = jwtSecret.getBytes();
	}

	public LoginResponse login(LoginRequest req) {
		LoginResponse resp = new LoginResponse();

		logger.info("Login attempt: schoolId={}", req.getSchoolId());
		Optional<User> found = userRepository.findBySchoolId(req.getSchoolId());
		if (found.isEmpty()) {
			logger.warn("Login failed - user not found: {}", req.getSchoolId());
			resp.setSuccess(false);
			resp.setMessage("Invalid credentials");
			return resp;
		}
		User u = found.get();
		boolean ok = argon2.verify(u.getPasswordHash(), req.getPassword());
		if (!ok) {
			logger.warn("Login failed - bad credentials for schoolId={}", req.getSchoolId());
			resp.setSuccess(false);
			resp.setMessage("Invalid credentials");
			return resp;
		}

		u.setLastLogin(OffsetDateTime.now());
		userRepository.save(u);
		logger.info("Login successful for user id={}, schoolId={}", u.getId(), u.getSchoolId());

		resp.setSuccess(true);
		resp.setMessage("Login successful");
		resp.setMustChangePassword(!Boolean.TRUE.equals(u.getPasswordChanged()));
		// generate JWT
		String token = Jwts.builder()
				.setSubject(u.getSchoolId())
				.claim("id", u.getId())
				.claim("status", u.getAccountStatus().name())
				.setIssuedAt(java.util.Date.from(java.time.Instant.now()))
				.setExpiration(java.util.Date.from(java.time.Instant.now().plusMillis(jwtExpirationMs)))
				.signWith(Keys.hmacShaKeyFor(jwtSecretBytes), SignatureAlgorithm.HS256)
				.compact();
		logger.debug("JWT generated for user id={}", u.getId());
		resp.setToken(token);
		return resp;
	}

	public ChangePasswordResponse changePassword(ChangePasswordRequest req) {
		ChangePasswordResponse resp = new ChangePasswordResponse();

		logger.info("Change-password attempt for schoolId={}", req.getSchoolId());
		Optional<User> found = userRepository.findBySchoolId(req.getSchoolId());
		if (found.isEmpty()) {
			logger.warn("Change-password failed - user not found: {}", req.getSchoolId());
			resp.setSuccess(false);
			resp.setMessage("User not found");
			return resp;
		}
		User u = found.get();
		boolean ok = argon2.verify(u.getPasswordHash(), req.getOldPassword());
		if (!ok) {
			logger.warn("Change-password failed - incorrect old password for schoolId={}", req.getSchoolId());
			resp.setSuccess(false);
			resp.setMessage("Old password is incorrect");
			return resp;
		}

		String newHash = argon2.hash(3, 65536, 1, req.getNewPassword());
		u.setPasswordHash(newHash);
		u.setPasswordChanged(true);
		// upon changing from default to user password, activate the account
		u.setAccountStatus(AccountStatusType.ACTIVE);
		u.setDateUpdated(OffsetDateTime.now());
		userRepository.save(u);
		logger.info("Password changed for user id={}, schoolId={}", u.getId(), u.getSchoolId());

		resp.setSuccess(true);
		resp.setMessage("Password changed successfully");
		return resp;
	}

    
}
