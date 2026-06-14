package com.clinic.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinic.backend.Entity.Appointment;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    // Get all appointments of a doctor
    List<Appointment> findByDoctorId(Long doctorId);

    // Get appointments of a doctor by status
    List<Appointment> findByDoctorIdAndStatus(
            Long doctorId,
            String status);

    // Dashboard counts
    long countByStatus(String status);
}