package com.clinic.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.backend.Entity.Queue;

public interface QueueRepository extends JpaRepository<Queue, Long> {

    List<Queue> findAllByOrderByTokenNumberAsc();

    Optional<Queue> findFirstByStatusOrderByTokenNumberAsc(String status);

    Optional<Queue> findTopByOrderByTokenNumberDesc();

    Optional<Queue> findByAppointmentId(Long appointmentId);
}