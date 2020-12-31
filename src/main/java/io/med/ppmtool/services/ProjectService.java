package io.med.ppmtool.services;

import io.med.ppmtool.domain.Project;
import io.med.ppmtool.exceptions.ProjectIdentifierException;
import io.med.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project saveOrUpdateProject(Project project) {
        try {
            if (project.getId() != null && project.getIdentifier() != null) {
                Optional<Project> existing = projectRepository.findById(project.getId());
                if (!existing.isPresent()) {
                    throw new ProjectIdentifierException("Project id '" + project.getId() +"' does not exist");
                }
                project.setIdentifier(existing.get().getIdentifier());
            } else {
                assert project.getIdentifier() != null;
                project.setIdentifier(project.getIdentifier().toUpperCase());
            }
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdentifierException("Project Identifier already exist");
        }
    }

    public Project findByIdentifier(String identifier) {
        Project project = projectRepository.findByIdentifier(identifier.toUpperCase());
        if (project == null) {
            throw new ProjectIdentifierException("Project Identifier '" + identifier.toUpperCase()+"' does not exist");
        }
        return project;
    }

    public Iterable<Project> fetchAllProjects(String keyword) {
        if (keyword != null) {
            return projectRepository.findByKeyword(keyword.toUpperCase());
        }
        return projectRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public void deleteProjectByIdentifier(String identifier) {
        Project project = this.findByIdentifier(identifier);
        projectRepository.delete(project);
    }
}
