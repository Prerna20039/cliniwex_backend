package com.clinic.backend.controller;

import com.clinic.backend.dto.Patient.LoginRequest;
import com.clinic.backend.dto.Patient.LoginResponse;
import com.clinic.backend.dto.Patient.ProfileResponse;
import com.clinic.backend.dto.Patient.ProfileUpdateRequest;
import com.clinic.backend.dto.Patient.RegisterRequest;
import com.clinic.backend.dto.Patient.StatsResponse;
import com.clinic.backend.service.PatientService;
import com.clinic.backend.service.QueueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;
    private final QueueService queueService;

    public PatientController(PatientService patientService, QueueService queueService) {
        this.patientService = patientService;
        this.queueService = queueService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        System.out.println("REGISTER API HIT");
        String result = patientService.register(request);
        
        if (result.equals("Email already in use")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        System.out.println("LOGIN API HIT");
        LoginResponse response = patientService.login(
                request.getEmail(),
                request.getPassword()
        );
        
        if (response.getToken() == null) {
            return ResponseEntity.status(401).body(response);
        }
        return ResponseEntity.ok(response);
    }

    // GET /api/patient/profile?patientId=1
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam(required = false) Long patientId) {
        if (patientId == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Patient ID is required"));
        }

        try {
            ProfileResponse profile = patientService.getProfile(patientId);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/patient/profile?patientId=1
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestParam(required = false) Long patientId,
            @Valid @RequestBody ProfileUpdateRequest request) {
        
        if (patientId == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Patient ID is required"));
        }

        try {
            ProfileResponse profile = patientService.updateProfile(patientId, request);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/patient/stats?patientId=1
    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam(required = false) Long patientId) {
        if (patientId == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Patient ID is required"));
        }

        try {
            StatsResponse stats = queueService.getPatientStats(patientId);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}