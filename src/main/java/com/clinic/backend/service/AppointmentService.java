package com.clinic.backend.service;


import com.clinic.backend.dto.Patient.AppointmentRequest;
import com.clinic.backend.entity.Appointment;
import com.clinic.backend.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment bookAppointment(Long patientId,
                                       AppointmentRequest request) {

        long count =
                appointmentRepository.countByDoctorIdAndAppointmentDate(
                        request.getDoctorId(),
                        request.getAppointmentDate());

        Appointment appointment = new Appointment();

        appointment.setPatientId(patientId);
        appointment.setDoctorId(request.getDoctorId());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setStatus("PENDING");
        appointment.setTokenNumber((int) count + 1);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getMyAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public void cancelAppointment(Long appointmentId) {

        Appointment appointment =
                appointmentRepository.findById(appointmentId)
                        .orElseThrow(() ->
                                new RuntimeException("Appointment not found"));

        appointment.setStatus("CANCELLED");

        appointmentRepository.save(appointment);
    }
}
