package io.med.ppmtool.services;

import io.med.ppmtool.domain.Backlog;
import io.med.ppmtool.domain.Project;
import io.med.ppmtool.dto.ProjectDto;
import io.med.ppmtool.exceptions.ProjectIdentifierException;
import io.med.ppmtool.mappers.ProjectMapper;
import io.med.ppmtool.repositories.BacklogRepository;
import io.med.ppmtool.repositories.ProjectRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService {

    private ProjectMapper projectMapper = Mappers.getMapper(ProjectMapper.class);

    private ProjectRepository projectRepository;
    private BacklogRepository backlogRepository;
    @Autowired
    public ProjectService(ProjectRepository projectRepository, BacklogRepository backlogRepository) {
        this.projectRepository = projectRepository;
        this.backlogRepository = backlogRepository;
    }

    public Project saveOrUpdateProject(ProjectDto projectDto) {
        try {
            Project project = projectMapper.toEntity(projectDto);
            if (project.getId() == null) {
                // create
                String identifier = projectDto.getIdentifier().toUpperCase();
                Optional<Project> existing = projectRepository.findByIdentifier(identifier);
                if (existing.isPresent()) {
                    throw new ProjectIdentifierException("Project identifier already exist");
                }
                Backlog backlog = new Backlog();
                backlog.setProject(project);
                backlog.setProjectIdentifier(identifier);
                project.setIdentifier(identifier);
                project.setBacklog(backlog);
            } else {
                // update
                Optional<Project> existing = projectRepository.findById(project.getId());
                if (!existing.isPresent()) {
                    throw new ProjectIdentifierException("Project id '" + project.getId() +"' does not exist");
                }
                project = existing.get();
                projectMapper.updateProjectFromDto(projectDto, project);
            }
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdentifierException(e.getMessage());
        }
    }

    public Project findByIdentifier(String identifier) {
        Project project = projectRepository.findByIdentifier(identifier.toUpperCase()).orElse(null);
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
