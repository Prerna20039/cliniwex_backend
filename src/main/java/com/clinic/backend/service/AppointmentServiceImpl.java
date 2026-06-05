package com.clinic.backend.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinic.backend.Entity.Appointment;
import com.clinic.backend.Entity.Queue;
import com.clinic.backend.Repository.AppointmentRepository;
import com.clinic.backend.Repository.QueueRepository;
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
    public List<Appointment> getAllAppointments(Long doctorId) {
        return repo.findByDoctorId(doctorId);
    }

    @Override
    public List<Appointment> getPendingAppointments(Long doctorId) {
        return repo.findByDoctorIdAndStatus(doctorId, "PENDING");
    }

    @Override
    public Appointment updateStatus(Long appointmentId, String status) {

        Appointment appt = repo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!status.equals("ACCEPTED") &&
            !status.equals("REJECTED")) {

            throw new RuntimeException("Invalid status");
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