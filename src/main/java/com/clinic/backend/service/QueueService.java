package com.clinic.backend.Service;

import java.util.List;

import com.clinic.backend.Entity.Queue;

public interface QueueService {

    List<Queue> getQueue();

    Queue callNextPatient();

    Queue completeConsultation(Long appointmentId);
}