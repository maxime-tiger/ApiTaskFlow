package org.taskflow.apitaskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taskflow.apitaskflow.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByProjectId(Long projectId);
}
