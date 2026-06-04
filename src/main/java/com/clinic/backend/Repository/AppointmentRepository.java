package com.clinic.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.backend.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByDoctorIdAndStatus(Long doctorId, String status);
}