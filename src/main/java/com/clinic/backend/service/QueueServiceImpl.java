package com.clinic.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinic.backend.entity.Appointment;
import com.clinic.backend.entity.Queue;
import com.clinic.backend.repository.AppointmentRepository;
import com.clinic.backend.repository.QueueRepository;
import com.clinic.backend.dto.Patient.QueueResponse;

@Service
public class QueueServiceImpl implements QueueService {

    private final QueueRepository queueRepository;
    private final AppointmentRepository appointmentRepository;

    public QueueServiceImpl(
            QueueRepository queueRepository,
            AppointmentRepository appointmentRepository) {

        this.queueRepository = queueRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // =========================
    // GET QUEUE (DOCTOR BASED)
    // =========================
    @Override
    public List<Queue> getQueue(Long doctorId) {
        return queueRepository
                .findByDoctorIdAndStatusInOrderByTokenNumberAsc(
                        doctorId,
                        List.of("WAITING", "IN_PROGRESS")
                );
    }

    // =========================
    // CALL NEXT PATIENT
    // =========================
    @Override
    public Queue callNextPatient(Long doctorId) {

        Queue current = queueRepository
                .findFirstByDoctorIdAndStatus(doctorId, "IN_PROGRESS")
                .orElse(null);

        if (current != null) {
            throw new RuntimeException("Already a patient in consultation");
        }

        Queue next = queueRepository
                .findFirstByDoctorIdAndStatusOrderByTokenNumberAsc(doctorId, "WAITING")
                .orElseThrow(() -> new RuntimeException("No waiting patients"));

        next.setStatus("IN_PROGRESS");

        return queueRepository.save(next);
    }

    // =========================
    // COMPLETE CONSULTATION
    // =========================
    @Override
    public Queue completeConsultation(Long appointmentId) {

        Queue queue = queueRepository
                .findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Queue not found"));

        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        queue.setStatus("DONE");
        appointment.setStatus("COMPLETED");

        appointmentRepository.save(appointment);

        return queueRepository.save(queue);
    }

    // =========================
    // QUEUE STATUS
    // =========================
    @Override
    public QueueResponse getQueueStatus(Long appointmentId) {

        Queue queue = queueRepository
                .findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Queue record not found"));

        Queue current = queueRepository
                .findFirstByDoctorIdAndStatus(
                        queue.getDoctorId(),
                        "WAITING"
                )
                .orElse(queue);

        int position = queue.getTokenNumber() - current.getTokenNumber();

        QueueResponse response = new QueueResponse();

        response.setTokenNumber(queue.getTokenNumber());
        response.setCurrentToken(current.getTokenNumber());
        response.setPosition(Math.max(position, 0));
        response.setStatus(queue.getStatus());
        response.setEstimatedWaitingTime(Math.max(position, 0) * 10 + " mins");

        return response;
    }
}