package org.taskflow.apitaskflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.taskflow.apitaskflow.model.Project;
import org.taskflow.apitaskflow.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
	
	private final ProjectRepository projectRepository;
	
	@Autowired
	public ProjectServiceImpl(ProjectRepository projectRepository){
		this.projectRepository = projectRepository;
	}
	
	@Override
	public Project createProject(Project project) {
		return projectRepository.save(project);
	}
	
	@Override
	public Optional<Project> getProjectById(Long id) {
		return projectRepository.findById(id);
	}
	
	@Override
	public List<Project> getAllProjects() {
		return projectRepository.findAll();
	}
	
	@Override
	public Project updateProject(Project project) {
		return projectRepository.save(project);
	}
	
	@Override
	public void deleteProject(Long id) {
		projectRepository.deleteById(id);
	}
}
