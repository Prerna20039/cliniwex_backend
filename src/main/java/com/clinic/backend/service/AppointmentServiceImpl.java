package com.clinic.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinic.backend.entity.Appointment;
import com.clinic.backend.repository.AppointmentRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repo;

    public AppointmentServiceImpl(AppointmentRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Appointment> getAllAppointments(Long doctorId) {
        return repo.findByDoctorId(doctorId);
    }

    @Override
    public List<Appointment> getPendingAppointments(Long doctorId) {
        return repo.findByDoctorIdAndStatus(doctorId, "PENDING");
    }

    @Override
    public Appointment updateStatus(Long appointmentId, String status) {

        Appointment appt = repo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        if (!status.equals("ACCEPTED") && !status.equals("REJECTED")) {
            throw new RuntimeException("Invalid status");
        }

        appt.setStatus(status);
        return repo.save(appt);
    }
}