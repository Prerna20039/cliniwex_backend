package com.clinic.backend.service;

import java.util.List;
import java.util.Map;
import com.clinic.backend.entity.Queue;

import org.springframework.stereotype.Service;

import com.clinic.backend.entity.Appointment;
import com.clinic.backend.entity.Queue;
import com.clinic.backend.repository.AppointmentRepository;
import com.clinic.backend.repository.QueueRepository;
import com.clinic.backend.dto.Patient.QueueResponse;
import com.clinic.backend.dto.Patient.StatsResponse;

@Service
public class QueueServiceImpl implements QueueService {

    private final QueueRepository queueRepository;
    private final AppointmentRepository appointmentRepository;

    private static final int AVG_TREATMENT_TIME_MINUTES = 15;

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
    public QueueResponse getQueueStatus(Long appointmentId) {

        Queue queue = queueRepository
                .findByAppointmentId(appointmentId)
                .orElseThrow(() ->
                        new RuntimeException("Queue record not found"));

        Queue currentQueue = queueRepository
                .findTopByStatusOrderByTokenNumberAsc("WAITING")
                .orElse(queue);

        QueueResponse response = new QueueResponse();

        response.setTokenNumber(queue.getTokenNumber());
        response.setCurrentToken(currentQueue.getTokenNumber());

        int position =
                queue.getTokenNumber()
                - currentQueue.getTokenNumber();

        response.setPosition(Math.max(position, 0));

        response.setStatus(queue.getStatus());

        int waitingMinutes =
                Math.max(position, 0) * 10;

        response.setEstimatedWaitingTime(
                waitingMinutes + " mins");

        return response;
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


@Override
public StatsResponse getPatientStats(Long patientId) {
    Appointment appointment = appointmentRepository
        .findFirstByPatientIdAndStatusOrderByAppointmentDateDesc(patientId, "ACCEPTED")
        .orElseThrow(() -> new RuntimeException("No active appointment found"));

    Queue patientQueue = queueRepository
            .findByAppointmentId(appointment.getAppointmentId())
            .orElseThrow(() -> new RuntimeException("Patient not in queue"));

    Integer yourToken = patientQueue.getTokenNumber();

    Integer currentToken = queueRepository
            .findTopByStatusOrderByTokenNumberAsc("WAITING")
            .map(Queue::getTokenNumber)
            .orElse(yourToken);

    Long patientsAhead = queueRepository
            .countByStatusAndTokenNumberLessThan("WAITING", yourToken);

    int estimatedWaitMinutes = patientsAhead.intValue() * AVG_TREATMENT_TIME_MINUTES;

    StatsResponse response = new StatsResponse();
    response.setYourToken(yourToken);
    response.setCurrentToken(currentToken);
    response.setPatientsAhead(patientsAhead.intValue());
    response.setEstimatedWaitMinutes(estimatedWaitMinutes);

    return response;
}
}