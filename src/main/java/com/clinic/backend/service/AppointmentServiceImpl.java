package com.clinic.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinic.backend.dto.Patient.AppointmentRequest;
import com.clinic.backend.entity.Appointment;
import com.clinic.backend.entity.Queue;
import com.clinic.backend.repository.AppointmentRepository;
import com.clinic.backend.repository.QueueRepository;
@Service

public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repo;
    private final QueueRepository queueRepo;

    public AppointmentServiceImpl(
            AppointmentRepository repo,
            QueueRepository queueRepo) {

        this.repo = repo;
        this.queueRepo = queueRepo;
    }

    @Override
public Appointment bookAppointment(Long patientId, AppointmentRequest request) {
    long count = repo.countByDoctorIdAndAppointmentDate(
            request.getDoctorId(),
            request.getAppointmentDate());

    Appointment appointment = new Appointment();

    appointment.setPatientId(patientId);
    appointment.setDoctorId(request.getDoctorId());
    appointment.setAppointmentDate(request.getAppointmentDate());
    appointment.setAppointmentTime(request.getAppointmentTime());

    // Only keep this if Appointment has a reason field
    // appointment.setReason(request.getReason());

    appointment.setStatus("PENDING");
    appointment.setTokenNumber((int) count + 1);

    return repo.save(appointment);
}

@Override
public void cancelAppointment(Long appointmentId) {
    Appointment appointment = repo.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));

    appointment.setStatus("CANCELLED");
    repo.save(appointment);
}
    @Override
    public List<Appointment> getAllAppointments(Long doctorId) {
        return repo.findByDoctorId(doctorId);
    }

    @Override
    public List<Appointment> getPendingAppointments(Long doctorId) {
        return repo.findByDoctorIdAndStatus(doctorId, "PENDING");
    }

    @Override
    public List<Appointment> getMyAppointments(Long patientId) {
        return repo.findByPatientId(patientId);
    }

    @Override
    public Appointment updateStatus(Long appointmentId, String status) {

        Appointment appt = repo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!status.equals("ACCEPTED")
        && !status.equals("CANCELLED")) {

    throw new RuntimeException(
            "Status must be ACCEPTED or CANCELLED");
}

        appt.setStatus(status);

        if ("ACCEPTED".equals(status)) {

            int nextToken = 1;

            Queue lastQueue = queueRepo
                    .findTopByOrderByTokenNumberDesc()
                    .orElse(null);

            if (lastQueue != null) {
                nextToken = lastQueue.getTokenNumber() + 1;
            }

            appt.setTokenNumber(nextToken);

            Queue queue = new Queue();
            queue.setAppointmentId(appt.getAppointmentId());
            queue.setTokenNumber(nextToken);
            queue.setStatus("WAITING");

            queueRepo.save(queue);
        }

        return repo.save(appt);
    }
}