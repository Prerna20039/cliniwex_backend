package com.clinic.backend.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinic.backend.Entity.Appointment;
import com.clinic.backend.Entity.Queue;
import com.clinic.backend.Repository.AppointmentRepository;
import com.clinic.backend.Repository.QueueRepository;

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

    @Override
    public List<Queue> getQueue() {
        return queueRepository.findAllByOrderByTokenNumberAsc();
    }

    @Override
    public Queue callNextPatient() {

        Queue queue = queueRepository
                .findFirstByStatusOrderByTokenNumberAsc("WAITING")
                .orElseThrow();

        queue.setStatus("IN_PROGRESS");

        return queueRepository.save(queue);
    }

    @Override
public Queue completeConsultation(Long appointmentId) {

    Queue queue = queueRepository
            .findByAppointmentId(appointmentId)
            .orElseThrow(() ->
                    new RuntimeException("Queue not found for appointment " + appointmentId));

    Appointment appointment = appointmentRepository
            .findById(appointmentId)
            .orElseThrow(() ->
                    new RuntimeException("Appointment not found: " + appointmentId));

    queue.setStatus("DONE");
    appointment.setStatus("COMPLETED");

    appointmentRepository.save(appointment);

    return queueRepository.save(queue);
}
}