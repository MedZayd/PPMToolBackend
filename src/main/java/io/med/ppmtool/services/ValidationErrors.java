package io.med.ppmtool.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ValidationErrors {

    public ResponseEntity<Map<String, String>> getValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = bindingResult.getFieldErrors().stream().collect(Collectors.toMap(
                    FieldError::getField,
                    e -> {
                        if (e.getDefaultMessage() != null) {
                            return e.getDefaultMessage();
                        }
                        return "";
                    }
            ));
            return ResponseEntity.badRequest().body(fieldErrors);
        }
        return null;
    }
}
