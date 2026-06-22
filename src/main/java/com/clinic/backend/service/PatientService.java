package com.clinic.backend.service;

import com.clinic.backend.dto.Patient.LoginResponse;
import com.clinic.backend.dto.Patient.RegisterRequest;
import com.clinic.backend.entity.Patient;
import com.clinic.backend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        // Authenticate using Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // If authentication successful, generate JWT
        if (authentication.isAuthenticated()) {
            PatientDetails patientDetails = (PatientDetails) patientDetailsService.loadUserByUsername(email);
            String jwtToken = jwtService.generateToken(patientDetails);
            
            return new LoginResponse(
                    jwtToken,
                    "Login successful",
                    patientDetails.getPatient().getEmail(),
                    patientDetails.getPatient().getName()
            );
        }

        return new LoginResponse(null, "Invalid email or password", null, null);
    }
}