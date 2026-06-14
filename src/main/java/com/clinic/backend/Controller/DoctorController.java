package com.clinic.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.backend.entity.Appointment;
import com.clinic.backend.entity.Queue;
import com.clinic.backend.service.AppointmentService;
import com.clinic.backend.service.DoctorService;
import com.clinic.backend.dto.LoginRequest;
import com.clinic.backend.dto.RegisterRequest;
import com.clinic.backend.dto.StatusUpdateRequest;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    public DoctorController(
            DoctorService doctorService,
            AppointmentService appointmentService) {

        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    // Doctor Registration
    @PostMapping("/register")
    public String registerDoctor(@RequestBody RegisterRequest request) {

        return doctorService.registerDoctor(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
    }

    // Doctor Login
    @PostMapping("/login")
    public String loginDoctor(@RequestBody LoginRequest request) {

        return doctorService.loginDoctor(
                request.getEmail(),
                request.getPassword()
        );
    }

    // Get all appointments for a doctor
    @GetMapping("/appointments")
    public List<Appointment> getAppointments(
            @RequestParam Long doctorId) {

        return appointmentService.getAllAppointments(doctorId);
    }

    // Accept / Reject Appointment
    @PutMapping("/appointments/{id}/status")
    public Appointment updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {

        return appointmentService.updateStatus(
                id,
                request.getStatus()
        );
    }

    // Doctor Dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {

        return ResponseEntity.ok(
                doctorService.getDashboardStats()
        );
    }

    // Call Next Patient
    @PutMapping("/queue/call-next")
    public ResponseEntity<?> callNextPatient() {

        return ResponseEntity.ok(
                doctorService.callNextPatient()
        );
    }


    // Complete Consultation
    @PutMapping("/queue/complete/{queueId}")
    public ResponseEntity<?> completeConsultation(
            @PathVariable Long queueId) {

        return ResponseEntity.ok(
                doctorService.completeConsultation(queueId)
        );
}

        @GetMapping("/queue")
        public List<Queue> getQueue() {

            return doctorService.getQueue();
        }
        @GetMapping("/queue/stats")
public ResponseEntity<?> getQueueStats() {

    return ResponseEntity.ok(
            doctorService.getQueueStats()
    );
}

}