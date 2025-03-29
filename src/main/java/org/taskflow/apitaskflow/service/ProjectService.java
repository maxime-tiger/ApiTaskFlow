package org.taskflow.apitaskflow.service;

import org.taskflow.apitaskflow.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
	Project createProject(Project project);
	Optional<Project> getProjectById(Long id);
	List<Project> getAllProjects();
	Project updateProject(Project project);
	void deleteProject(Long id);
}
