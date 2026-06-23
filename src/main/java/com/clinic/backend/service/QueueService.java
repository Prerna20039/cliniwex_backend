package com.clinic.backend.service;

import java.util.List;

import com.clinic.backend.dto.Patient.QueueResponse;
import com.clinic.backend.entity.Queue;

public interface QueueService {

    List<Queue> getQueue(Long doctorId);

    Queue callNextPatient(Long doctorId);

    Queue completeConsultation(Long appointmentId);

    QueueResponse getQueueStatus(Long appointmentId);
}