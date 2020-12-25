package io.med.ppmtool.web;

import io.med.ppmtool.domain.Project;
import io.med.ppmtool.services.ProjectService;
import io.med.ppmtool.services.ValidationErrors;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private ProjectService projectService;
    private ValidationErrors validationErrors;

    @Autowired
    public ProjectController(ProjectService projectService, ValidationErrors validationErrors) {
        this.projectService = projectService;
        this.validationErrors = validationErrors;
    }

    @PostMapping
    public ResponseEntity<?> createNewProject(
            @Valid @RequestBody Project project,
            BindingResult bindingResult
    ) {
        ResponseEntity<Map<String, String>> errorMap = validationErrors.getValidationErrors(bindingResult);
        if (errorMap != null) return errorMap;
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.saveOrUpdateProject(project));
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<Project> findProjectByIdentifier(@PathVariable String identifier) {
        Project project = projectService.findByIdentifier(identifier);
        return ResponseEntity.ok().body(project);
    }
}
