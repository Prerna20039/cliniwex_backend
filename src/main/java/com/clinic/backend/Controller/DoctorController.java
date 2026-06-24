package com.clinic.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clinic.backend.dto.AnalyticsResponse;
import com.clinic.backend.dto.DoctorProfileResponse;
import com.clinic.backend.dto.DoctorUpdateRequest;
import com.clinic.backend.dto.LoginRequest;
import com.clinic.backend.dto.RegisterRequest;
import com.clinic.backend.dto.StatusUpdateRequest;
import com.clinic.backend.entity.Appointment;
import com.clinic.backend.entity.Doctor;
import com.clinic.backend.entity.Queue;
import com.clinic.backend.repository.DoctorRepository;
import com.clinic.backend.service.AgentService;
import com.clinic.backend.service.AppointmentService;
import com.clinic.backend.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;

    @Autowired
    private AgentService agentService;

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
    public List<Appointment> getAppointments(@RequestParam Long doctorId) {
        return appointmentService.getAllAppointments(doctorId);
    }

    @PutMapping("/appointments/{id}/status")
    public Appointment updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {

        return appointmentService.updateStatus(id, request.getStatus());
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        return ResponseEntity.ok(doctorService.getDashboardStats());
    }

    // ================= QUEUE =================
    @PutMapping("/queue/call-next")
    public ResponseEntity<?> callNextPatient() {
        return ResponseEntity.ok(doctorService.callNextPatient());
    }

    @PutMapping("/queue/complete/{queueId}")
    public ResponseEntity<?> completeConsultation(@PathVariable Long queueId) {
        return ResponseEntity.ok(doctorService.completeConsultation(queueId));
    }

    @GetMapping("/queue")
    public List<Queue> getQueue() {
        return doctorService.getQueue();
    }

    @GetMapping("/queue/stats")
    public ResponseEntity<?> getQueueStats() {
        return ResponseEntity.ok(doctorService.getQueueStats());
    }

    // ================= LIVE TOGGLE =================
    @PatchMapping("/{id}/live")
public ResponseEntity<?> toggleLive(@PathVariable Long id) {

    Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));

    boolean current = Boolean.TRUE.equals(doctor.getIsLive());
    doctor.setIsLive(!current);

    if (doctor.getIsLive()) {
        agentService.processDoctorLive(id);
    }

    return ResponseEntity.ok(doctorRepository.save(doctor));
}

    // ================= PROFILE (EMAIL BASED) =================


// ================= PROFILE GET (EMAIL ONLY) =================
@GetMapping("/profile")
public ResponseEntity<DoctorProfileResponse> getProfile(
        @RequestParam String email) {

    return ResponseEntity.ok(
            doctorService.getDoctorProfileByEmail(email));
}

// ================= PROFILE UPDATE (EMAIL ONLY) =================
@PutMapping("/profile")
public ResponseEntity<DoctorProfileResponse> updateProfile(
        @RequestParam String email,
        @RequestBody DoctorUpdateRequest request) {

    return ResponseEntity.ok(
            doctorService.updateDoctorProfileByEmail(email, request));
}

    // ================= ANALYTICS =================
    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics() {
        return ResponseEntity.ok(doctorService.getAnalytics());
    }
}