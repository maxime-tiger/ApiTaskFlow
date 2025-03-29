package org.taskflow.apitaskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taskflow.apitaskflow.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
