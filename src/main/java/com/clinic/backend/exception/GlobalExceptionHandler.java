package com.clinic.backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateAppointmentException.class)
    public ResponseEntity<?> handleDuplicate(DuplicateAppointmentException e) {
        return ResponseEntity.status(409).body(Map.of(
            "success", false,
            "message", e.getMessage()
        ));
    }

    // Fallback: catches database unique constraint violation
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrity(DataIntegrityViolationException e) {
        if (e.getMessage() != null && e.getMessage().contains("uk_patient_date")) {
            return ResponseEntity.status(409).body(Map.of(
                "success", false,
                "message", "You already have an appointment on this date"
            ));
        }
        return ResponseEntity.status(500).body(Map.of(
            "success", false,
            "message", "Database error occurred"
        ));
    }
}