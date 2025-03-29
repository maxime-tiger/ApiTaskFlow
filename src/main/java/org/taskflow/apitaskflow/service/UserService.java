package org.taskflow.apitaskflow.service;

import org.taskflow.apitaskflow.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
	User createUser(User user);
	Optional<User> getUserById(Long id);
	Optional<User> getUserByEmail(String email);
	List<User> getAllUsers();
	User updateUser(User user);
	void deleteUser(Long id);
}
