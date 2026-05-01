package com.et.expense_tracker_backed.auth.controller;

import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.et.expense_tracker_backed.auth.dto.LoginRequest;
import com.et.expense_tracker_backed.auth.dto.RegisterRequest;
import com.et.expense_tracker_backed.config.JwtService;
import com.et.expense_tracker_backed.entity.User;
import com.et.expense_tracker_backed.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthController(UserRepository userRepository, 
						  PasswordEncoder passwordEncoder, 
						  JwtService jwtService) {
		this.repository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		if (repository.existsByEmail(request.getEmail())) {
			return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
		}

		User user = new User();
		user.setName(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		repository.save(user);

		return ResponseEntity.status(201).body(Map.of("message", "User Registered Sucessfully"));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {

		Optional<User> optionalUser = repository.findByEmail(request.getEmail());

		if (request.getEmail() == null || request.getPassword() == null) {
			return ResponseEntity.badRequest().body(Map.of("message", "Email and password required"));
		}

		if (optionalUser.isEmpty()) {
			return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
		}

		User user = optionalUser.get();

		if (user.getPassword() == null) {
			return ResponseEntity.status(500).body(Map.of("message", "Password not stored correctly"));
		}

		boolean isMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());

		if (!isMatch) {
			return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
		}

		String token = jwtService.generateToken(user.getEmail());

		return ResponseEntity.ok(Map.of("message", "Login Successful",
										"token", token,
										"userId", user.getId(),
										"username", user.getName()));
	}
}
