package org.taskflow.apitaskflow.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.taskflow.apitaskflow.model.Task;
import org.taskflow.apitaskflow.model.TaskStatus;
import org.taskflow.apitaskflow.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
	
	@Mock
	private TaskRepository taskRepository;
	
	@InjectMocks
	private TaskServiceImpl taskService;
	
	@Test
	void testCreateTask() {
		Task task = new Task();
		task.setTitle("Test Task");
		task.setDescription("Test Description");
		task.setStatus(TaskStatus.TODO);
		task.setDeadline(LocalDateTime.now().plusDays(1));
		
		when(taskRepository.save(task)).thenReturn(task);
		Task createdTask = taskService.createTask(task);
		assertNotNull(createdTask);
		assertEquals("Test Task", createdTask.getTitle());
		verify(taskRepository, times(1)).save(task);
	}
	
	@Test
	void testGetTaskById() {
		Task task = new Task();
		task.setId(1L);
		task.setTitle("Test Task");
		
		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
		Optional<Task> foundTask = taskService.getTaskById(1L);
		assertTrue(foundTask.isPresent());
		assertEquals(1L, foundTask.get().getId());
		verify(taskRepository, times(1)).findById(1L);
	}
	
	@Test
	void testGetTasksByProjectId() {
		Task task1 = new Task();
		task1.setId(1L);
		task1.setTitle("Task 1");
		Task task2 = new Task();
		task2.setId(2L);
		task2.setTitle("Task 2");
		
		List<Task> tasks = List.of(task1, task2);
		
		when(taskRepository.findByProjectId(1L)).thenReturn(tasks);
		List<Task> foundTasks = taskService.getTasksByProjectId(1L);
		
		assertNotNull(foundTasks);
		assertEquals(2, foundTasks.size());
		assertEquals("Task 1", foundTasks.get(0).getTitle());
		assertEquals("Task 2", foundTasks.get(1).getTitle());
		verify(taskRepository, times(1)).findByProjectId(1L);
	}
	
	@Test
	void testUpdateTask() {
		Task task = new Task();
		task.setId(1L);
		task.setTitle("Old Title");
		task.setStatus(TaskStatus.TODO);
		
		Task updatedTask = new Task();
		updatedTask.setId(1L);
		updatedTask.setTitle("New Title");
		updatedTask.setStatus(TaskStatus.IN_PROGRESS);
		
		when(taskRepository.save(updatedTask)).thenReturn(updatedTask);
		
		Task result = taskService.updateTask(updatedTask);
		
		assertNotNull(result);
		assertEquals("New Title", result.getTitle());
		assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
		verify(taskRepository, times(1)).save(updatedTask);
	}
	
	@Test
	void testDeleteTask() {
		Long taskId = 1L;
		
		doNothing().when(taskRepository).deleteById(taskId);
		
		taskService.deleteTask(taskId);
		
		verify(taskRepository, times(1)).deleteById(taskId);
	}
}