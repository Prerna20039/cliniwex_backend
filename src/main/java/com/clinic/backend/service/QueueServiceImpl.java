package com.clinic.backend.service;

import java.util.List;
import java.util.Map;
import com.clinic.backend.entity.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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
            .findFirstByDoctorIdAndStatus(
                    doctorId,
                    "IN_PROGRESS"
            )
            .orElse(null);

    if (current != null) {
        throw new RuntimeException(
                "Already a patient in consultation"
        );
    }

    Queue next = queueRepository
            .findFirstByDoctorIdAndStatusOrderByTokenNumberAsc(
                    doctorId,
                    "WAITING"
            )
            .orElseThrow(() ->
                    new RuntimeException(
                            "No waiting patients"
                    ));

    next.setStatus("IN_PROGRESS");

Queue savedQueue = queueRepository.save(next);

// TEMP TEST
Appointment appointment = appointmentRepository
        .findById(savedQueue.getAppointmentId())
        .orElse(null);

if (appointment != null) {
    sendPatientStatsUpdate(
            appointment.getPatientId()
    );
}

return savedQueue;
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
                        "IN_PROGRESS"
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

@Override
public Queue completeConsultation(Long appointmentId) {

    Queue queue = queueRepository
            .findByAppointmentId(appointmentId)
            .orElseThrow(() ->
                    new RuntimeException(
                            "Queue not found for appointment "
                                    + appointmentId
                    ));

    Appointment appointment =
            appointmentRepository
                    .findById(appointmentId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Appointment not found: "
                                            + appointmentId
                            ));

    queue.setStatus("DONE");

    appointment.setStatus("COMPLETED");

    appointmentRepository.save(appointment);

    Queue savedQueue =
            queueRepository.save(queue);

    notifyAllWaitingPatients(
            queue.getDoctorId()
    );

    return savedQueue;
}
private void sendPatientStatsUpdate(Long patientId) {

    try {

        StatsResponse stats = getPatientStats(patientId);
        System.out.println(
            "Sending websocket update to patient: "
            + patientId
        );

        messagingTemplate.convertAndSend(
                "/topic/stats/" + patientId,
                stats
        );

        

    } catch (Exception e) {

        System.out.println(
                "Unable to send websocket update for patient "
                        + patientId
        );
    }
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
        .findFirstByDoctorIdAndStatus(
                patientQueue.getDoctorId(),
                "IN_PROGRESS"
        )
        .map(Queue::getTokenNumber)
        .orElseGet(() ->
                queueRepository
                        .findFirstByDoctorIdAndStatusOrderByTokenNumberAsc(
                                patientQueue.getDoctorId(),
                                "WAITING"
                        )
                        .map(Queue::getTokenNumber)
                        .orElse(0)
        );

    Long patientsAhead = queueRepository
            .countByStatusAndTokenNumberLessThan("WAITING", yourToken);

    int estimatedWaitMinutes = patientsAhead.intValue() * 10;

    StatsResponse response = new StatsResponse();
    response.setYourToken(yourToken);
    response.setCurrentToken(currentToken);
    response.setPatientsAhead(patientsAhead.intValue());
    response.setEstimatedWaitMinutes(estimatedWaitMinutes);

    return response;
}

private void notifyAllWaitingPatients(Long doctorId) {

    List<Queue> queues =
            queueRepository
                    .findByDoctorIdAndStatusInOrderByTokenNumberAsc(
                            doctorId,
                            List.of("WAITING", "IN_PROGRESS")
                    );

    for (Queue queue : queues) {

        Appointment appointment =
                appointmentRepository
                        .findById(queue.getAppointmentId())
                        .orElse(null);

        if (appointment != null) {

            sendPatientStatsUpdate(
                    appointment.getPatientId()
            );
        }
    }
}
}