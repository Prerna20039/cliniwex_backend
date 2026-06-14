package com.clinic.backend.service;

import java.util.List;

import com.clinic.backend.dto.Patient.AppointmentRequest;
import com.clinic.backend.entity.Appointment;

public interface AppointmentService {

    Appointment bookAppointment(Long patientId, AppointmentRequest request);

    List<Appointment> getMyAppointments(Long patientId);

    void cancelAppointment(Long appointmentId);

    List<Appointment> getAllAppointments(Long doctorId);

    List<Appointment> getPendingAppointments(Long doctorId);

    Appointment updateStatus(Long appointmentId, String status);

    
}