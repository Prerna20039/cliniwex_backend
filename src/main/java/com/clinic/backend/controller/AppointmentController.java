package com.clinic.backend.controller;

import com.clinic.backend.dto.Patient.AppointmentRequest;
import com.clinic.backend.entity.Appointment;
import com.clinic.backend.exception.DuplicateAppointmentException;
import com.clinic.backend.service.AppointmentService;
import com.clinic.backend.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Book Appointment - prevents duplicates
    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequest request) {
        Long patientId = SecurityUtils.getCurrentPatientId();

        if (patientId == null) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "User not authenticated"
            ));
        }

        try {
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
        }
    }

    // Get My Appointments
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

    // Your existing endpoints...
    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getAllAppointments(@PathVariable Long doctorId) {
        return appointmentService.getAllAppointments(doctorId);
    }

    @GetMapping("/doctor/{doctorId}/pending")
    public List<Appointment> getPendingAppointments(@PathVariable Long doctorId) {
        return appointmentService.getPendingAppointments(doctorId);
    }

    @PutMapping("/{appointmentId}/status")
    public Appointment updateStatus(@PathVariable Long appointmentId, @RequestParam String status) {
        return appointmentService.updateStatus(appointmentId, status);
    }

    @PutMapping("/{appointmentId}/cancel")
    public void cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
    }
}