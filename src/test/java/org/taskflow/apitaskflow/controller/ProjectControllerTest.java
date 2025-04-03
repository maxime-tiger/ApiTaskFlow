package org.taskflow.apitaskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.taskflow.apitaskflow.model.Project;
import org.taskflow.apitaskflow.security.CustomUserDetailsService;
import org.taskflow.apitaskflow.security.JwtUtil;
import org.taskflow.apitaskflow.service.ProjectService;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false) // Désactive les filtres de sécurité pour le test
class ProjectControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ProjectService projectService;
	
	@MockBean
	private JwtUtil jwtUtil;
	
	@MockBean
	private CustomUserDetailsService customUserDetailsService;
	
	@Test
	void testGetProjectById() throws Exception {
		Project project = new Project();
		project.setId(1L);
		project.setName("Test Project");
		project.setDescription("Test Description");
		
		when(projectService.getProjectById(1L)).thenReturn(Optional.of(project));
		
		mockMvc.perform(get("/api/projects/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(project.getId()))
				.andExpect(jsonPath("$.name").value(project.getName()))
				.andExpect(jsonPath("$.description").value(project.getDescription()));
	}
	
	@Test
	void testGetAllProjects() throws Exception {
		Project project1 = new Project();
		project1.setId(1L);
		project1.setName("Project 1");
		project1.setDescription("Description 1");
		
		Project project2 = new Project();
		project2.setId(2L);
		project2.setName("Project 2");
		project2.setDescription("Description 2");
		
		when(projectService.getAllProjects()).thenReturn(Arrays.asList(project1, project2));
		
		mockMvc.perform(get("/api/projects")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(project1.getId()))
				.andExpect(jsonPath("$[0].name").value(project1.getName()))
				.andExpect(jsonPath("$[1].id").value(project2.getId()))
				.andExpect(jsonPath("$[1].name").value(project2.getName()));
	}
	
	// Vous pouvez ajouter ici d'autres tests pour les endpoints POST, PUT, DELETE...
	
	@Test
	void testCreateProject() throws Exception {
		Project newProject = new Project();
		newProject.setId(1L);
		newProject.setName("New Project");
		newProject.setDescription("New Project Description");
		
		when(projectService.createProject(newProject)).thenReturn(newProject);
		
		mockMvc.perform(post("/api/projects")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newProject)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(newProject.getId()))
				.andExpect(jsonPath("$.name").value(newProject.getName()))
				.andExpect(jsonPath("$.description").value(newProject.getDescription()));
	}
	
	@Test
	void testUpdateProject() throws Exception {
		Project existingProject = new Project();
		existingProject.setId(1L);
		existingProject.setName("Existing Project");
		existingProject.setDescription("Existing Description");
		
		Project updatedProject = new Project();
		updatedProject.setId(1L);
		updatedProject.setName("Updated Project");
		updatedProject.setDescription("Updated Description");
		
		when(projectService.updateProject(existingProject)).thenReturn(updatedProject);
		
		mockMvc.perform(put("/api/projects")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(existingProject)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(updatedProject.getId()))
				.andExpect(jsonPath("$.name").value(updatedProject.getName()))
				.andExpect(jsonPath("$.description").value(updatedProject.getDescription()));
	}
	
	@Test
	void testDeleteProjectSuccess() throws Exception {
		Long projectId = 1L;
		
		// Simulate successful deletion
		mockMvc.perform(delete("/api/projects/{id}", projectId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent()); // Expecting 204 status
	}
}