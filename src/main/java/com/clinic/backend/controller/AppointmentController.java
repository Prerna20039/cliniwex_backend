package com.clinic.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.backend.dto.Patient.AppointmentRequest;
import com.clinic.backend.entity.Appointment;
import com.clinic.backend.entity.Doctor;
import com.clinic.backend.repository.DoctorRepository;
import com.clinic.backend.service.AppointmentService;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;

    public AppointmentController(AppointmentService appointmentService,
                                  DoctorRepository doctorRepository) {
        this.appointmentService = appointmentService;
        this.doctorRepository = doctorRepository;
    }

    // ✅ BOOK APPOINTMENT (WITH LIVE CHECK)
    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequest request) {

        Long patientId = 1L; // TODO: replace with JWT authentication

        // 🔍 Check doctor exists
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // 🔴 Check if doctor is live
        if (!doctor.getIsLive()) {
    return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body("Doctor is not live. Cannot book appointment.");
}

        Appointment appointment =
                appointmentService.bookAppointment(patientId, request);

        return ResponseEntity.ok(appointment);
    }

    // ✅ GET MY APPOINTMENTS
    @GetMapping("/my")
    public ResponseEntity<List<Appointment>> getMyAppointments() {

        Long patientId = 1L; // TODO: replace with JWT

        return ResponseEntity.ok(
                appointmentService.getMyAppointments(patientId)
        );
    }

    // ✅ CANCEL APPOINTMENT
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {

        appointmentService.cancelAppointment(id);

        return ResponseEntity.ok("Appointment Cancelled");
    }
}