package org.taskflow.apitaskflow.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.taskflow.apitaskflow.model.User;
import org.taskflow.apitaskflow.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UserServiceImplTest {
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private UserServiceImpl userService;
	
	public UserServiceImplTest() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testCreateUser_SuccessfulCreation() {
		// Arrange
		User user = new User();
		user.setId(1L);
		
		Mockito.when(userService.createUser(any(User.class))).thenReturn(user);
		
		// Act
		User createdUser = userService.createUser(user);
		
		// Assert
		assertNotNull(createdUser);
		assertEquals(1L, createdUser.getId());
	}
	
	@Test
	void testGetUserById_UserExists() {
		// Arrange
		User user = new User();
		user.setId(1L);
		
		Mockito.when(userService.getUserById(1L)).thenReturn(Optional.of(user));
		
		// Act
		Optional<User> retrievedUser = userService.getUserById(1L);
		
		// Assert
		assertNotNull(retrievedUser);
		assertEquals(1L, retrievedUser.get().getId());
	}
	
	@Test
	void testGetUserById_UserNotFound() {
		// Arrange
		Mockito.when(userRepository.findById(2L)).thenReturn(Optional.empty());
		
		// Act
		Optional<User> retrievedUser = userService.getUserById(2L);
		
		// Assert
		assertNotNull(retrievedUser);
		assertEquals(Optional.empty(), retrievedUser);
	}
	
	@Test
	void testGetUserByEmail_UserExists() {
		// Arrange
		User user = new User();
		user.setId(1L);
		
		Mockito.when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));
		
		// Act
		Optional<User> retrievedUser = userService.getUserByEmail("test@example.com");
		
		// Assert
		assertNotNull(retrievedUser);
		assertEquals(1L, retrievedUser.get().getId());
	}
	
	@Test
	void testGetUserByEmail_UserNotFound() {
		// Arrange
		Mockito.when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
		
		// Act
		Optional<User> retrievedUser = userService.getUserByEmail("notfound@example.com");
		
		// Assert
		assertNotNull(retrievedUser);
		assertEquals(Optional.empty(), retrievedUser);
	}
	
	@Test
	void testGetAllUsers_UserExists() {
		// Arrange
		User user1 = new User();
		user1.setId(1L);
		User user2 = new User();
		user2.setId(2L);
		
		Mockito.when(userService.getAllUsers()).thenReturn(List.of(user1, user2));
		
		// Act
		List<User> users = userService.getAllUsers();
		
		// Assert
		assertNotNull(users);
		assertEquals(2, users.size());
		assertEquals(1L, users.get(0).getId());
		assertEquals(2L, users.get(1).getId());
	}
	
	@Test
	void testGetAllUsers_NoUsers() {
		// Arrange
		Mockito.when(userRepository.findAll()).thenReturn(List.of());
		
		// Act
		List<User> users = userService.getAllUsers();
		
		// Assert
		assertNotNull(users);
		assertEquals(0, users.size());
	}
	
	@Test
	void testUpdateUser_UserExists_SuccessfulUpdate() {
		// Arrange
		User existingUser = new User();
		existingUser.setId(1L);
		
		User updatedUser = new User();
		updatedUser.setId(1L);
		
		Mockito.when(userService.updateUser(any(User.class))).thenReturn(updatedUser);
		
		// Act
		User result = userService.updateUser(updatedUser);
		
		// Assert
		assertNotNull(result);
		assertEquals(1L, result.getId());
	}
	
	@Test
	void testDeleteUser_UserExists() {
		// Arrange
		Long userId = 1L;
		Mockito.doNothing().when(userRepository).deleteById(userId);
		
		// Act
		userService.deleteUser(userId);
		
		// Assert
		verify(userRepository, never()).deleteById(userId);
	}
	
	@Test
	void testDeleteUser_UserDoesNotExist() {
		// Arrange
		Long userId = 999L;
		Mockito.doNothing().when(userRepository).deleteById(userId);
		
		// Act
		userService.deleteUser(userId);
		
		// Assert
		verify(userRepository, never()).deleteById(anyLong());
	}
}