package org.taskflow.apitaskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taskflow.apitaskflow.model.User;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
