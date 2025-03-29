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
import org.taskflow.apitaskflow.model.Project;
import org.taskflow.apitaskflow.service.ProjectService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
	
	private final ProjectService projectService;
	
	@Autowired
	public ProjectController(ProjectService projectService){
		this.projectService = projectService;
	}
	
	@PostMapping
	public ResponseEntity<Project> createProject(@RequestBody Project project) {
		Project createdProject = projectService.createProject(project);
		return ResponseEntity.ok(createdProject);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
		Optional<Project> projectOpt = projectService.getProjectById(id);
		return projectOpt.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@GetMapping
	public ResponseEntity<List<Project>> getAllProjects() {
		List<Project> projects = projectService.getAllProjects();
		return ResponseEntity.ok(projects);
	}
	
	@PutMapping
	public ResponseEntity<Project> updateProject(@RequestBody Project project) {
		Project updatedProject = projectService.updateProject(project);
		return ResponseEntity.ok(updatedProject);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
		projectService.deleteProject(id);
		return ResponseEntity.noContent().build();
	}
}
