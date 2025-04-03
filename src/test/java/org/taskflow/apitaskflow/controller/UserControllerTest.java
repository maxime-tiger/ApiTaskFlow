package org.taskflow.apitaskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.taskflow.apitaskflow.model.User;
import org.taskflow.apitaskflow.security.CustomUserDetailsService;
import org.taskflow.apitaskflow.security.JwtUtil;
import org.taskflow.apitaskflow.service.UserService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Désactive les filtres de sécurité pour le test
class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private JwtUtil jwtUtil;
	
	@MockBean
	private CustomUserDetailsService customUserDetailsService;
	
	@Test
	void testGetUserById() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Test User");
		user.setEmail("test@example.com");
		
		when(userService.getUserById(1L)).thenReturn(Optional.of(user));
		
		mockMvc.perform(get("/api/users/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(user.getId()))
				.andExpect(jsonPath("$.name").value(user.getName()))
				.andExpect(jsonPath("$.email").value(user.getEmail()));
	}
	
	// Vous pouvez ajouter ici d'autres tests pour les endpoints POST, PUT, DELETE...
}