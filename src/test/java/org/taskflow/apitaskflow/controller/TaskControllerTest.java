package org.taskflow.apitaskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.taskflow.apitaskflow.model.Task;
import org.taskflow.apitaskflow.model.TaskStatus;
import org.taskflow.apitaskflow.security.CustomUserDetailsService;
import org.taskflow.apitaskflow.security.JwtUtil;
import org.taskflow.apitaskflow.service.TaskService;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TaskService taskService;
	
	@MockBean
	private JwtUtil jwtUtil;
	
	@MockBean
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	void testGetTaskById() throws Exception {
		Task task = new Task();
		task.setId(1L);
		task.setTitle("Sample Task");
		task.setDescription("Description");
		task.setStatus(TaskStatus.TODO);
		task.setDeadline(LocalDateTime.now().plusDays(1));
		
		when(taskService.getTaskById(1L)).thenReturn(java.util.Optional.of(task));
		
		mockMvc.perform(get("/api/tasks/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
}