package org.taskflow.apitaskflow.service;

import org.taskflow.apitaskflow.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
	Task createTask(Task task);
	Optional<Task> getTaskById(Long id);
	List<Task> getTasksByProjectId(Long projectId);
	Task updateTask(Task task);
	void deleteTask(Long id);
}
