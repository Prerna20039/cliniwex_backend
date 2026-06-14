package com.clinic.backend.service;

import java.util.List;

import com.clinic.backend.entity.Queue;
import com.clinic.backend.dto.Patient.QueueResponse;

public interface QueueService {

    List<Queue> getQueue();

    Queue callNextPatient();

    Queue completeConsultation(Long appointmentId);

    QueueResponse getQueueStatus(Long appointmentId);
}