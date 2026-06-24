package com.clinic.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.backend.dto.DoctorProfileResponse;
import com.clinic.backend.dto.LoginRequest;
import com.clinic.backend.dto.RegisterRequest;
import com.clinic.backend.dto.StatusUpdateRequest;
import com.clinic.backend.entity.Appointment;
import com.clinic.backend.entity.Doctor;
import com.clinic.backend.entity.Queue;
import com.clinic.backend.repository.DoctorRepository;
import com.clinic.backend.service.AppointmentService;
import com.clinic.backend.service.DoctorService;
import com.clinic.backend.dto.AnalyticsResponse;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;

    public DoctorController(
            DoctorService doctorService,
            AppointmentService appointmentService,
            DoctorRepository doctorRepository) {

        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.doctorRepository = doctorRepository;
    }

    // ================= REGISTER =================

    @PostMapping("/register")
    public String registerDoctor(@RequestBody RegisterRequest request) {
        return doctorService.registerDoctor(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getSpecialty(),
                request.getPhone());
    }

    // ================= LOGIN =================

    @PostMapping("/login")
    public String loginDoctor(@RequestBody LoginRequest request) {
        return doctorService.loginDoctor(
                request.getEmail(),
                request.getPassword());
    }

    // ================= APPOINTMENTS =================

    @GetMapping("/appointments")
    public List<Appointment> getAppointments(
            @RequestParam Long doctorId) {

        return appointmentService.getAllAppointments(doctorId);
    }

    @PutMapping("/appointments/{id}/status")
    public Appointment updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {

        return appointmentService.updateStatus(
                id,
                request.getStatus());
    }

    // ================= DASHBOARD =================

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        return ResponseEntity.ok(
                doctorService.getDashboardStats());
    }

    // ================= QUEUE =================

    @PutMapping("/queue/call-next")
    public ResponseEntity<?> callNextPatient() {
        return ResponseEntity.ok(
                doctorService.callNextPatient());
    }

    @PutMapping("/queue/complete/{queueId}")
    public ResponseEntity<?> completeConsultation(
            @PathVariable Long queueId) {

        return ResponseEntity.ok(
                doctorService.completeConsultation(queueId));
    }

    @GetMapping("/queue")
    public List<Queue> getQueue() {
        return doctorService.getQueue();
    }

    @GetMapping("/queue/stats")
    public ResponseEntity<?> getQueueStats() {
        return ResponseEntity.ok(
                doctorService.getQueueStats());
    }

    // ================= LIVE TOGGLE =================

    @PatchMapping("/{id}/live")
public ResponseEntity<?> toggleLive(@PathVariable Long id) {

    Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));

    boolean current = Boolean.TRUE.equals(doctor.getIsLive());
    doctor.setIsLive(!current);

    return ResponseEntity.ok(doctorRepository.save(doctor));
}

    // ================= PROFILE =================

@GetMapping("/profile/{doctorId}")
public ResponseEntity<?> getDoctorProfile(
        @PathVariable Long doctorId) {

    try {

        DoctorProfileResponse response =
                doctorService.getDoctorProfile(doctorId);

        return ResponseEntity.ok(response);

    } catch (Exception e) {

        e.printStackTrace();

        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }
}

    @PutMapping("/profile/{doctorId}")
    public ResponseEntity<Doctor> updateDoctorProfile(
            @PathVariable Long doctorId,
            @RequestBody Doctor doctor) {

        return ResponseEntity.ok(
                doctorService.updateDoctorProfile(
                        doctorId,
                        doctor));
    }

    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics() {

        return ResponseEntity.ok(
                doctorService.getAnalytics());
    }
}