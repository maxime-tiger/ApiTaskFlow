package org.taskflow.apitaskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.taskflow.apitaskflow.model.User;
import org.taskflow.apitaskflow.security.CustomUserDetailsService;
import org.taskflow.apitaskflow.security.JwtUtil;
import org.taskflow.apitaskflow.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
	void testGetAllUsers() throws Exception {
		User user1 = new User();
		user1.setId(1L);
		user1.setName("User One");
		user1.setEmail("user1@example.com");
		
		User user2 = new User();
		user2.setId(2L);
		user2.setName("User Two");
		user2.setEmail("user2@example.com");
		
		List<User> users = List.of(user1, user2);
		
		when(userService.getAllUsers()).thenReturn(users);
		
		mockMvc.perform(get("/api/users")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(user1.getId()))
				.andExpect(jsonPath("$[0].name").value(user1.getName()))
				.andExpect(jsonPath("$[0].email").value(user1.getEmail()))
				.andExpect(jsonPath("$[1].id").value(user2.getId()))
				.andExpect(jsonPath("$[1].name").value(user2.getName()))
				.andExpect(jsonPath("$[1].email").value(user2.getEmail()));
	}
	
	@Test
	void testGetAllUsersEmpty() throws Exception {
		when(userService.getAllUsers()).thenReturn(List.of());
		
		mockMvc.perform(get("/api/users")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());
	}
	
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
	
	@Test
	void testCreateUser() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Test User");
		user.setEmail("test@example.com");
		
		when(userService.createUser(any(User.class))).thenReturn(user);
		
		mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(user.getId()))
				.andExpect(jsonPath("$.name").value(user.getName()))
				.andExpect(jsonPath("$.email").value(user.getEmail()));
	}
	
	@Test
	void testGetUserByEmail() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Test User");
		user.setEmail("test@example.com");
		
		when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));
		
		mockMvc.perform(get("/api/users/email/test@example.com")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(user.getId()))
				.andExpect(jsonPath("$.name").value(user.getName()))
				.andExpect(jsonPath("$.email").value(user.getEmail()));
	}
	
	@Test
	void testGetUserByEmailNotFound() throws Exception {
		when(userService.getUserByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
		
		mockMvc.perform(get("/api/users/email/nonexistent@example.com")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	void testUpdateUserSuccess() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Updated User");
		user.setEmail("updated@example.com");
		
		when(userService.updateUser(any(User.class))).thenReturn(user);
		
		mockMvc.perform(put("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(user.getId()))
				.andExpect(jsonPath("$.name").value(user.getName()))
				.andExpect(jsonPath("$.email").value(user.getEmail()));
	}
	
	@Test
	void testDeleteUserSuccess() throws Exception {
		mockMvc.perform(delete("/api/users/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}
}