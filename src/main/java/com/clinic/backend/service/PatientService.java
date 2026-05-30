package com.clinic.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinic.backend.dto.Patient.RegisterRequest;
import com.clinic.backend.repository.PatientRepository;
import com.clinic.backend.entity.Patient;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;


    public String register(RegisterRequest request) {
        // Check if email already exists
        if (patientRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already in use";
        }

        // Create new patient and save to database
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setPassword(request.getPassword()); // In production, hash the password!

        patientRepository.save(patient);
        return "Registration successful";
    }
    

    public String login(String email, String password) {
        // Find patient by email
        Patient patient = patientRepository.findByEmail(email).orElse(null);
        if (patient == null) {
            return "Invalid email or password";
        }

        // Check password (in production, use hashed passwords)
        if (!patient.getPassword().equals(password)) {
            return "Invalid email or password";
        }

        return "Login successful";
    }
    
}
