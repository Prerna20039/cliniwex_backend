package com.clinic.backend.Service;

import java.util.List;

import com.clinic.backend.Entity.Appointment;

public interface AppointmentService {

    List<Appointment> getAllAppointments(Long doctorId);

    List<Appointment> getPendingAppointments(Long doctorId);

    Appointment updateStatus(Long appointmentId, String status);
}