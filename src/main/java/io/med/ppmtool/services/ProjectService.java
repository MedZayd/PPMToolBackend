package io.med.ppmtool.services;

import io.med.ppmtool.domain.Project;
import io.med.ppmtool.exceptions.ProjectIdentifierException;
import io.med.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project saveOrUpdateProject(Project project) {
        try {
            project.setIdentifier(project.getIdentifier().toUpperCase());
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdentifierException("Project Identifier '" + project.getIdentifier().toUpperCase()+"' already exist");
        }
    }

    public Project findByIdentifier(String identifier) {
        Project project = projectRepository.findByIdentifier(identifier.toUpperCase());
        if (project == null) {
            throw new ProjectIdentifierException("Project Identifier '" + identifier.toUpperCase()+"' does not exist");
        }
        return project;
    }

    public Iterable<Project> fetchAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String identifier) {
        Project project = this.findByIdentifier(identifier);
        projectRepository.delete(project);
    }
}
