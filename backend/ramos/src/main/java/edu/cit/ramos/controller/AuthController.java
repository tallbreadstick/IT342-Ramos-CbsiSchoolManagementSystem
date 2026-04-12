package edu.cit.ramos.controller;

import edu.cit.ramos.dto.request.ChangePasswordRequest;
import edu.cit.ramos.dto.request.LoginRequest;
import edu.cit.ramos.dto.response.ChangePasswordResponse;
import edu.cit.ramos.dto.response.LoginResponse;
import edu.cit.ramos.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	private final AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}



	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
		logger.info("Login attempt: schoolId={}", req.getSchoolId());
		LoginResponse resp = authService.login(req);
		if (!resp.isSuccess()) {
			logger.warn("Login failed for schoolId={}: {}", req.getSchoolId(), resp.getMessage());
			return ResponseEntity.status(401).body(resp);
		}
		logger.info("Login succeeded for schoolId={}; mustChangePassword={}", req.getSchoolId(), resp.isMustChangePassword());
		return ResponseEntity.ok(resp);
	}

	@PostMapping("/change-password")
	public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody ChangePasswordRequest req) {
		logger.info("Change-password request: schoolId={}", req.getSchoolId());
		ChangePasswordResponse resp = authService.changePassword(req);
		if (!resp.isSuccess()) {
			logger.warn("Change-password failed for schoolId={}: {}", req.getSchoolId(), resp.getMessage());
			return ResponseEntity.badRequest().body(resp);
		}
		logger.info("Password changed successfully for schoolId={}", req.getSchoolId());
		return ResponseEntity.ok(resp);
	}
}
