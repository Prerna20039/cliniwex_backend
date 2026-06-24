package com.clinic.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.clinic.backend.entity.Queue;

public interface QueueRepository extends JpaRepository<Queue, Long> {

    // =====================================================
    // DOCTOR-WISE QUEUE
    // =====================================================

    Optional<Queue> findFirstByDoctorIdAndStatus(Long doctorId, String status);

    Optional<Queue> findFirstByDoctorIdAndStatusOrderByTokenNumberAsc(Long doctorId, String status);

    List<Queue> findByDoctorIdOrderByTokenNumberAsc(Long doctorId);

    List<Queue> findByDoctorIdAndStatusInOrderByTokenNumberAsc(Long doctorId, List<String> statuses);

    // =====================================================
    // BASIC COUNTS (DASHBOARD)
    // =====================================================

    long countByStatus(String status);

    long countByStatusIn(List<String> statuses);

    long countByDoctorIdAndStatus(Long doctorId, String status);

    long countByDoctorIdAndStatusIn(Long doctorId, List<String> statuses);

    // =====================================================
    // FINDERS
    // =====================================================

    Optional<Queue> findByStatus(String status);

    Optional<Queue> findByAppointmentId(Long appointmentId);

    Optional<Queue> findByDoctorIdAndAppointmentId(Long doctorId, Long appointmentId);

    // =====================================================
    // TOKEN HANDLING
    // =====================================================

    Optional<Queue> findTopByOrderByTokenNumberDesc();

    Optional<Queue> findFirstByStatusOrderByTokenNumberAsc(String status);

    Optional<Queue> findTopByStatusOrderByTokenNumberAsc(String status);

    

    // =====================================================
    // LISTING
    // =====================================================

    List<Queue> findAllByOrderByTokenNumberAsc();

    List<Queue> findByStatusInOrderByTokenNumberAsc(List<String> statuses);

    // Find current token (lowest WAITING token_number)
    Optional<Queue> findCurrentToken();

    

    

    // Count patients ahead (WAITING with token < given token)
    Long countByStatusAndTokenNumberLessThan(String status, Integer tokenNumber);

    // =====================================================
    // DAILY RESET (MIDNIGHT JOB)
    // =====================================================

    @Modifying
    @Transactional
    @Query("UPDATE Queue q SET q.status = 'WAITING'")
    void resetQueue();

    // =====================================================
    // 90 DAYS CLEANUP
    // =====================================================

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM queue
        WHERE created_at < NOW() - INTERVAL '90 days'
    """, nativeQuery = true)
    int deleteQueueOlderThan90Days();
}