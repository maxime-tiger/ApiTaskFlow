package org.taskflow.apitaskflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.taskflow.apitaskflow.model.Task;
import org.taskflow.apitaskflow.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
	private final TaskRepository taskRepository;
	
	@Autowired
	public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	@Override
	public Task createTask(Task task) {
		return taskRepository.save(task);
	}
	
	@Override
	public Optional<Task> getTaskById(Long id) {
		return taskRepository.findById(id);
	}
	
	@Override
	public List<Task> getTasksByProjectId(Long projectId) {
		return taskRepository.findByProjectId(projectId);
	}
	
	@Override
	public Task updateTask(Task task) {
		return taskRepository.save(task);
	}
	
	@Override
	public void deleteTask(Long id) {
		taskRepository.deleteById(id);
	}
}
