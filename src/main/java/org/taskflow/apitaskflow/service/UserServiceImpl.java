package org.taskflow.apitaskflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.taskflow.apitaskflow.model.User;
import org.taskflow.apitaskflow.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	@Override
	public User createUser(User user) {
		return userRepository.save(user);
	}
	
	@Override
	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}
	
	@Override
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}
	
	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}
