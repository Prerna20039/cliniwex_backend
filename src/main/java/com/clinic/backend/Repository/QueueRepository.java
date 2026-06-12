package com.clinic.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.backend.Entity.Queue;

public interface QueueRepository extends JpaRepository<Queue, Long> {

    long countByStatus(String status);

    Optional<Queue> findByStatus(String status);

    Optional<Queue> findByAppointmentId(Long appointmentId);

    Optional<Queue> findTopByOrderByTokenNumberDesc();

    Optional<Queue> findFirstByStatusOrderByTokenNumberAsc(String status);

    List<Queue> findAllByOrderByTokenNumberAsc();

    List<Queue> findByStatusInOrderByTokenNumberAsc(
            List<String> statuses);
}