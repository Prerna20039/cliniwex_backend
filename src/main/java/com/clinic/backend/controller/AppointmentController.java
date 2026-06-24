package com.clinic.backend.controller;

import com.clinic.backend.dto.Patient.AppointmentRequest;
import com.clinic.backend.entity.Appointment;
import com.clinic.backend.entity.Doctor;
import com.clinic.backend.exception.DuplicateAppointmentException;
import com.clinic.backend.repository.DoctorRepository;
import com.clinic.backend.service.AppointmentService;
import com.clinic.backend.util.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;

    public AppointmentController(AppointmentService appointmentService,
                                  DoctorRepository doctorRepository) {
        this.appointmentService = appointmentService;
        this.doctorRepository = doctorRepository;
    }

    // ✅ BOOK APPOINTMENT (JWT + DOCTOR LIVE CHECK)
    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequest request) {
        // JWT auth
        Long patientId = SecurityUtils.getCurrentPatientId();

        if (patientId == null) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "User not authenticated"
            ));
        }

        try {
            // Doctor live check (your partner's code)
            Doctor doctor = doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));

            if (!doctor.getIsLive()) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                            "success", false,
                            "message", "Doctor is not live. Cannot book appointment."
                        ));
            }

            Appointment appointment = appointmentService.bookAppointment(patientId, request);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Appointment booked successfully",
                "appointmentId", appointment.getAppointmentId(),
                "tokenNumber", appointment.getTokenNumber()
            ));

        } catch (DuplicateAppointmentException e) {
            return ResponseEntity.status(409).body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    // ✅ GET MY APPOINTMENTS (JWT)
    @GetMapping("/my")
    public ResponseEntity<?> getMyAppointments() {
        Long patientId = SecurityUtils.getCurrentPatientId();

        if (patientId == null) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "User not authenticated"
            ));
        }

        List<Appointment> appointments = appointmentService.getMyAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }

    // ✅ GET ALL APPOINTMENTS FOR DOCTOR
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAllAppointments(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAllAppointments(doctorId));
    }

    // ✅ GET PENDING APPOINTMENTS FOR DOCTOR
    @GetMapping("/doctor/{doctorId}/pending")
    public ResponseEntity<List<Appointment>> getPendingAppointments(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getPendingAppointments(doctorId));
    }

    // ✅ UPDATE APPOINTMENT STATUS
    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable Long appointmentId,
            @RequestParam String status) {
        return ResponseEntity.ok(appointmentService.updateStatus(appointmentId, status));
    }

    // ✅ CANCEL APPOINTMENT
    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Appointment cancelled successfully"
        ));
    }
}