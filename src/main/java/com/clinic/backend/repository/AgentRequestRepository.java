package com.clinic.backend.repository;

import com.clinic.backend.entity.AgentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentRequestRepository extends JpaRepository<AgentRequest, Long> {
    List<AgentRequest> findByDoctorIdAndStatus(Long doctorId, String status);
}