package io.med.ppmtool.exceptions;

import io.med.ppmtool.exceptions.dto.ProjectIdentifierExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleProjectIdentifierException(
            ProjectIdentifierException exception,
            WebRequest request
    ) {
        ProjectIdentifierExceptionResponse exceptionResponse =
                new ProjectIdentifierExceptionResponse(exception.getMessage());
        return ResponseEntity.badRequest().body(exceptionResponse);
    }
}
