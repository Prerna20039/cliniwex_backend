package com.clinic.backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.clinic.backend.Entity.Doctor;
import com.clinic.backend.Repository.DoctorRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public String registerDoctor(String name, String email, String password) {

        if (doctorRepository.existsByEmail(email)) {
            return "Email already exists";
        }

        Doctor doctor = new Doctor();

        doctor.setName(name);
        doctor.setEmail(email);
        doctor.setPassword(password);
        doctor.setCreatedAt(LocalDateTime.now());

        doctorRepository.save(doctor);

        return "Doctor Registered Successfully";
    }

    public String loginDoctor(String email, String password) {

        Doctor doctor = doctorRepository
                .findByEmail(email)
                .orElse(null);

        if (doctor == null) {
            return "Doctor not found";
        }

        if (!doctor.getPassword().equals(password)) {
            return "Invalid Password";
        }

        return "Login Successful";
    }
}