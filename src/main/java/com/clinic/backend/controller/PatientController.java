package com.clinic.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.clinic.backend.dto.Patient.LoginRequest;
import com.clinic.backend.dto.Patient.RegisterRequest;
import com.clinic.backend.service.PatientService;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService = new PatientService();

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

        System.out.println("REGISTER API HIT");

        return patientService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        System.out.println("LOGIN API HIT");

        return patientService.login(
                request.getEmail(),
                request.getPassword()
        );
    }
}