package com.clinic.backend.service;

import com.clinic.backend.dto.Patient.LoginResponse;
import com.clinic.backend.dto.Patient.ProfileResponse;
import com.clinic.backend.dto.Patient.ProfileUpdateRequest;
import com.clinic.backend.dto.Patient.RegisterRequest;
import com.clinic.backend.entity.Patient;
import com.clinic.backend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.clinic.backend.dto.Patient.ProfileUpdateRequest;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PatientDetailsService patientDetailsService;

    public String register(RegisterRequest request) {
        // Check if email already exists
        if (patientRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already in use";
        }

        // Create new patient with encoded password
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setPassword(passwordEncoder.encode(request.getPassword())); // Hash the password!

        patientRepository.save(patient);
        return "Registration successful";
    }

    public LoginResponse login(String email, String password) {

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
    );

    if (authentication.isAuthenticated()) {

        PatientDetails patientDetails =
                (PatientDetails) patientDetailsService.loadUserByUsername(email);

        String jwtToken = jwtService.generateToken(patientDetails);

        return new LoginResponse(
                jwtToken,
                "Login successful",
                patientDetails.getPatient().getEmail(),
                patientDetails.getPatient().getName(),
                patientDetails.getPatient().getId()
        );
    }

    return new LoginResponse(
            null,
            "Invalid email or password",
            null,
            null,
            null
    );
}
    
    public ProfileResponse getProfile(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));

        return new ProfileResponse(
            patient.getId(),
            patient.getName(),
            patient.getEmail(),
            patient.getPhone(),
            patient.getAge()
        );
    }

    
    @Transactional
    public ProfileResponse updateProfile(Long patientId, ProfileUpdateRequest request) {
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Update fields
        patient.setName(request.getName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setAge(request.getAge());

        Patient updated = patientRepository.save(patient);

        return new ProfileResponse(
            updated.getId(),
            updated.getName(),
            updated.getEmail(),
            updated.getPhone(),
            updated.getAge()
        );
    }
}