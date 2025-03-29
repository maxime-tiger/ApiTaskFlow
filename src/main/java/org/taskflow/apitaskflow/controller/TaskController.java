package org.taskflow.apitaskflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.taskflow.apitaskflow.model.Task;
import org.taskflow.apitaskflow.service.TaskService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
	
	private final TaskService taskService;
	
	@Autowired
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}
	
	@PostMapping
	public ResponseEntity<Task> createTask(@RequestBody Task task) {
		Task createdTask = taskService.createTask(task);
		return ResponseEntity.ok(createdTask);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
		Optional<Task> taskOpt = taskService.getTaskById(id);
		return taskOpt.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@GetMapping("/project/{projectId}")
	public ResponseEntity<List<Task>> getTasksByProjectId(@PathVariable Long projectId) {
		List<Task> tasks = taskService.getTasksByProjectId(projectId);
		return ResponseEntity.ok(tasks);
	}
	
	@PutMapping
	public ResponseEntity<Task> updateTask(@RequestBody Task task) {
		Task updatedTask = taskService.updateTask(task);
		return ResponseEntity.ok(updatedTask);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		taskService.deleteTask(id);
		return ResponseEntity.noContent().build();
	}
}
