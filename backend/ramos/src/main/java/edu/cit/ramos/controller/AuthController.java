package edu.cit.ramos.controller;

import edu.cit.ramos.dto.request.ChangePasswordRequest;
import edu.cit.ramos.dto.request.LoginRequest;
import edu.cit.ramos.dto.request.RegisterRequest;
import edu.cit.ramos.dto.response.ChangePasswordResponse;
import edu.cit.ramos.dto.response.LoginResponse;
import edu.cit.ramos.dto.response.RegisterResponse;
import edu.cit.ramos.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest req) {
		RegisterResponse resp = authService.register(req);
		if (resp.getId() == null) {
			return ResponseEntity.badRequest().body(resp);
		}
		return ResponseEntity.ok(resp);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
		LoginResponse resp = authService.login(req);
		if (!resp.isSuccess()) {
			return ResponseEntity.status(401).body(resp);
		}
		return ResponseEntity.ok(resp);
	}

	@PostMapping("/change-password")
	public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody ChangePasswordRequest req) {
		ChangePasswordResponse resp = authService.changePassword(req);
		if (!resp.isSuccess()) {
			return ResponseEntity.badRequest().body(resp);
		}
		return ResponseEntity.ok(resp);
	}
}
