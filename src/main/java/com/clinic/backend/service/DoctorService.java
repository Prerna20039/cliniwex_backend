package com.clinic.backend.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.clinic.backend.Entity.Appointment;
import com.clinic.backend.Entity.Doctor;
import com.clinic.backend.Entity.Queue;
import com.clinic.backend.Repository.AppointmentRepository;
import com.clinic.backend.Repository.DoctorRepository;
import com.clinic.backend.Repository.QueueRepository;
import com.clinic.backend.dto.DashboardResponse;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final QueueRepository queueRepository;

    public DoctorService(
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            QueueRepository queueRepository) {

        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.queueRepository = queueRepository;
    }

    public String registerDoctor(String name, String email, String password) {

        if (doctorRepository.existsByEmail(email)) {
            return "Email already exists";
        }

        Doctor doctor = new Doctor();

        doctor.setName(name);
        doctor.setEmail(email);
        doctor.setPassword(password);
        doctor.setCreatedAt(LocalDateTime.now());

        doctorRepository.save(doctor);

        return "Doctor Registered Successfully";
    }

    public String loginDoctor(String email, String password) {

        Doctor doctor = doctorRepository
                .findByEmail(email)
                .orElse(null);

        if (doctor == null) {
            return "Doctor not found";
        }

        if (!doctor.getPassword().equals(password)) {
            return "Invalid Password";
        }

        return "Login Successful";
    }
    public List<Queue> getQueue() {

    return queueRepository
            .findByStatusInOrderByTokenNumberAsc(
                    List.of("WAITING", "IN_PROGRESS"));
}

    public DashboardResponse getDashboardStats() {

        long totalAppointments =
                appointmentRepository.count();

        long pendingAppointments =
                appointmentRepository.countByStatus("PENDING");

        long acceptedAppointments =
                appointmentRepository.countByStatus("ACCEPTED");

        long completedAppointments =
                appointmentRepository.countByStatus("COMPLETED");

        long cancelledAppointments =
                appointmentRepository.countByStatus("CANCELLED");

        long patientsInQueue =
                queueRepository.countByStatus("WAITING");

        return new DashboardResponse(
                totalAppointments,
                pendingAppointments,
                acceptedAppointments,
                completedAppointments,
                cancelledAppointments,
                patientsInQueue
        );
    }

    public String callNextPatient() {

        Optional<Queue> currentPatient =
                queueRepository.findByStatus("IN_PROGRESS");

        if (currentPatient.isPresent()) {
            return "Patient already in consultation";
        }

        Optional<Queue> nextPatient =
                queueRepository
                        .findFirstByStatusOrderByTokenNumberAsc("WAITING");

        if (nextPatient.isEmpty()) {
            return "No patients waiting";
        }

        Queue queue = nextPatient.get();

        queue.setStatus("IN_PROGRESS");

        queueRepository.save(queue);

        return "Patient moved to consultation";
    }

    public String completeConsultation(Long queueId) {

        Queue queue =
                queueRepository.findById(queueId)
                        .orElse(null);

        if (queue == null) {
            return "Queue record not found";
        }

        if (!queue.getStatus().equals("IN_PROGRESS")) {
            return "Only IN_PROGRESS patient can be completed";
        }

        queue.setStatus("DONE");
        queueRepository.save(queue);

        Appointment appointment =
                appointmentRepository
                        .findById(queue.getAppointmentId())
                        .orElse(null);

        if (appointment != null) {

            appointment.setStatus("COMPLETED");

            appointmentRepository.save(appointment);
        }

        return "Consultation completed successfully";
    }
    public Map<String, Long> getQueueStats() {

    Map<String, Long> stats = new HashMap<>();

    stats.put("waiting",
            queueRepository.countByStatus("WAITING"));

    stats.put("inProgress",
            queueRepository.countByStatus("IN_PROGRESS"));

    stats.put("completed",
            queueRepository.countByStatus("DONE"));

    return stats;
}
}