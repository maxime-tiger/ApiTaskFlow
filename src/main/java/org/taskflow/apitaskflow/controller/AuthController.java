package org.taskflow.apitaskflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.taskflow.apitaskflow.model.JwtResponse;
import org.taskflow.apitaskflow.model.LoginRequest;
import org.taskflow.apitaskflow.model.RegistrationRequest;
import org.taskflow.apitaskflow.security.JwtUtil;
import org.taskflow.apitaskflow.model.User;
import org.taskflow.apitaskflow.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getEmail(),
						loginRequest.getPassword()
				)
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtil.generateJwtToken(loginRequest.getEmail());
		return ResponseEntity.ok(new JwtResponse(jwt));
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
		if(userService.getUserByEmail(registrationRequest.getEmail()).isPresent()){
			return ResponseEntity.badRequest().body("Error: Email is already in use!");
		}
		User newUser = new User();
		newUser.setName(registrationRequest.getName());
		newUser.setEmail(registrationRequest.getEmail());
		newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
		userService.createUser(newUser);
		return ResponseEntity.ok("User registered successfully!");
	}
}
