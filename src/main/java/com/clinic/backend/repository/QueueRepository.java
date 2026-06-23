package com.clinic.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.clinic.backend.entity.Queue;

public interface QueueRepository extends JpaRepository<Queue, Long> {

    long countByStatus(String status);

    Optional<Queue> findByStatus(String status);

    Optional<Queue> findByAppointmentId(Long appointmentId);

    Optional<Queue> findTopByOrderByTokenNumberDesc();

    Optional<Queue> findFirstByStatusOrderByTokenNumberAsc(String status);

    List<Queue> findAllByOrderByTokenNumberAsc();

    List<Queue> findByStatusInOrderByTokenNumberAsc(
            List<String> statuses);

    Optional<Queue> findTopByStatusOrderByTokenNumberAsc(String status);

    // Find current token (lowest WAITING token_number)
    Optional<Queue> findCurrentToken();

    // Count patients ahead of given token
    Long countPatientsAhead(@Param("tokenNumber") Integer tokenNumber);

    // Find patient's queue entry by patientId (through appointment)
    Optional<Queue> findPatientQueueEntry(@Param("patientId") Long patientId);

    // Count patients ahead (WAITING with token < given token)
    Long countByStatusAndTokenNumberLessThan(String status, Integer tokenNumber);

}