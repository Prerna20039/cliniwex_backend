package com.clinic.backend.controller;

import com.clinic.backend.dto.Patient.LoginRequest;
import com.clinic.backend.dto.Patient.LoginResponse;
import com.clinic.backend.dto.Patient.RegisterRequest;
import com.clinic.backend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clinic.backend.dto.Patient.StatsResponse;


import java.util.Map;
import com.clinic.backend.service.QueueService;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private QueueService queueService;

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

    // Example protected endpoint - requires valid JWT
    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        return ResponseEntity.ok("This is a protected endpoint. You have valid JWT!");
    }



    // GET /api/patient/stats?patientId=1
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