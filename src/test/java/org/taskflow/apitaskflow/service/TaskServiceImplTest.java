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
}