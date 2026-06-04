package com.clinic.backend.service;

import java.util.List;

import com.clinic.backend.entity.Appointment;

public interface AppointmentService {

    List<Appointment> getAllAppointments(Long doctorId);

    List<Appointment> getPendingAppointments(Long doctorId);

    Appointment updateStatus(Long appointmentId, String status);
}