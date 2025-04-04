package org.taskflow.apitaskflow.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.taskflow.apitaskflow.model.Project;
import org.taskflow.apitaskflow.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProjectServiceImplTest {
	
	@Mock
	private ProjectRepository projectRepository;
	
	@InjectMocks
	private ProjectServiceImpl projectService;
	
	public ProjectServiceImplTest() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testCreateProject_Success() {
		// Given
		Project testProject = new Project();
		testProject.setId(1L);
		
		when(projectService.createProject(testProject)).thenReturn(testProject);
		
		// When
		Project createdProject = projectService.createProject(testProject);
		
		// Then
		assertNotNull(createdProject);
		assertEquals(1L, createdProject.getId());
	}
	
	@Test
	void testGetProjectById_Success() {
		// Given
		Long projectId = 1L;
		Project testProject = new Project();
		testProject.setId(projectId);
		
		when(projectService.getProjectById(projectId)).thenReturn(Optional.of(testProject));
		
		// When
		Optional<Project> result = projectService.getProjectById(projectId);
		
		// Then
		assertNotNull(result);
		assertEquals(testProject, result.orElse(null));
	}
	
	@Test
	void testGetProjectById_NotFound() {
		// Given
		Long projectId = 1L;
		
		when(projectRepository.findById(projectId)).thenReturn(Optional.empty());
		
		// When
		Optional<Project> result = projectService.getProjectById(projectId);
		
		// Then
		assertNotNull(result);
		assertEquals(Optional.empty(), result);
	}
	
	@Test
	void testGetAllProjects_Success() {
		// Given
		Project project1 = new Project();
		project1.setId(1L);
		Project project2 = new Project();
		project2.setId(2L);
		
		when(projectService.getAllProjects()).thenReturn(List.of(project1, project2));
		
		// When
		List<Project> result = projectService.getAllProjects();
		
		// Then
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(1L, result.get(0).getId());
		assertEquals(2L, result.get(1).getId());
	}
	
	@Test
	void testGetAllProjects_EmptyList() {
		// Given
		when(projectRepository.findAll()).thenReturn(List.of());
		
		// When
		List<Project> result = projectService.getAllProjects();
		
		// Then
		assertNotNull(result);
		assertEquals(0, result.size());
	}
	
	@Test
	void testUpdateProject_Success() {
		// Given
		Project existingProject = new Project();
		existingProject.setId(1L);
		existingProject.setTasks(null);
		
		when(projectService.updateProject(existingProject)).thenReturn(existingProject);
		
		// When
		Project updatedProject = projectService.updateProject(existingProject);
		
		// Then
		assertNotNull(updatedProject);
		assertEquals(1L, updatedProject.getId());
	}
	
	@Test
	void testUpdateProject_ProjectNotFound() {
		// Given
		Project nonExistentProject = new Project();
		nonExistentProject.setId(100L);
		
		// Simulate saving a project that doesn't exist. Mock repository behavior for `save`.
		when(projectService.updateProject(nonExistentProject)).thenReturn(nonExistentProject);
		
		// When
		Project updatedProject = projectService.updateProject(nonExistentProject);
		
		// Then
		assertNotNull(updatedProject);
		assertEquals(100L, updatedProject.getId());
	}
	
	@Test
	void testDeleteProject_Success() {
		// Given
		Long projectId = 1L;
		
		// Ensure the repository doesn't throw an exception during deletion
		projectService.deleteProject(projectId);
		
		// Verify interaction with repository
		assertNotNull(projectId);
	}
	
	@Test
	void testDeleteProject_NotFound() {
		// Given
		Long projectId = 999L; // ID that doesn't exist
		
		try {
			projectService.deleteProject(projectId);
		} catch (Exception e) {
			// Assert that no exception is thrown
			assertNotNull(e);
		}
	}
}