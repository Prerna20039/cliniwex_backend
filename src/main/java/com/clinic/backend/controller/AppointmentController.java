package com.clinic.backend.controller;


import com.clinic.backend.dto.Patient.AppointmentRequest;
import com.clinic.backend.entity.Appointment;
import com.clinic.backend.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Book Appointment
    @PostMapping
    public Appointment bookAppointment(
            @RequestBody AppointmentRequest request) {

        Long patientId = 1L; // Replace with JWT logged-in user

        return appointmentService.bookAppointment(patientId, request);
    }

    // View My Appointments
    @GetMapping("/my")
    public List<Appointment> getMyAppointments() {

        Long patientId = 1L; // Replace with JWT logged-in user

        return appointmentService.getMyAppointments(patientId);
    }

    // Cancel Appointment
    @DeleteMapping("/{id}")
    public String cancelAppointment(@PathVariable Long id) {

        appointmentService.cancelAppointment(id);

        return "Appointment Cancelled";
    }
}
