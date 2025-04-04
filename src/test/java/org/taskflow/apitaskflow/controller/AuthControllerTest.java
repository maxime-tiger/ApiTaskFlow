package org.taskflow.apitaskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.taskflow.apitaskflow.model.LoginRequest;
import org.taskflow.apitaskflow.model.RegistrationRequest;
import org.taskflow.apitaskflow.model.User;
import org.taskflow.apitaskflow.security.CustomUserDetailsService;
import org.taskflow.apitaskflow.security.JwtUtil;
import org.taskflow.apitaskflow.service.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Désactive les filtres de sécurité pour le test
class AuthControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AuthenticationManager authenticationManager;
	
	@MockBean
	private JwtUtil jwtUtil;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private PasswordEncoder passwordEncoder;
	
	@MockBean
	private CustomUserDetailsService customUserDetailsService;
	
	@Test
	void authenticateUser_ValidCredentials_ReturnsJwtResponse() throws Exception {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("test@example.com");
		loginRequest.setPassword("securePassword");
		
		Authentication authentication = Mockito.mock(Authentication.class);
		
		when(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
		)).thenReturn(authentication);
		
		when(jwtUtil.generateJwtToken(loginRequest.getEmail())).thenReturn("mockedJwtToken");
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("mockedJwtToken"));
	}
	
	@Test
	void registerUser_EmailAlreadyInUse_ReturnsBadRequest() throws Exception {
		RegistrationRequest registrationRequest = new RegistrationRequest();
		registrationRequest.setName("Test User");
		registrationRequest.setEmail("test@example.com");
		registrationRequest.setPassword("securePassword");
		
		when(userService.getUserByEmail("test@example.com")).thenReturn(java.util.Optional.of(new User()));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(registrationRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Error: Email is already in use!"));
	}
	
	@Test
	void registerUser_ValidData_ReturnsSuccessMessage() throws Exception {
		RegistrationRequest registrationRequest = new RegistrationRequest();
		registrationRequest.setName("Test User");
		registrationRequest.setEmail("test@example.com");
		registrationRequest.setPassword("securePassword");
		
		when(userService.getUserByEmail("test@example.com")).thenReturn(java.util.Optional.empty());
		when(passwordEncoder.encode("securePassword")).thenReturn("encodedPassword");
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(registrationRequest)))
				.andExpect(status().isOk())
				.andExpect(content().string("User registered successfully!"));
	}
}