package com.clinic.backend.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.clinic.backend.dto.AnalyticsResponse;
import com.clinic.backend.dto.DashboardResponse;
import com.clinic.backend.dto.DoctorProfileResponse;
import com.clinic.backend.dto.DoctorUpdateRequest;
import com.clinic.backend.entity.Appointment;
import com.clinic.backend.entity.Doctor;
import com.clinic.backend.entity.Queue;
import com.clinic.backend.repository.AppointmentRepository;
import com.clinic.backend.repository.DoctorRepository;
import com.clinic.backend.repository.QueueRepository;

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

    // ================= REGISTER DOCTOR =================
    public String registerDoctor(
            String name,
            String email,
            String password,
            String specialty,
            String phone) {

        if (doctorRepository.existsByEmail(email)) {
            return "Email already exists";
        }

        Doctor doctor = new Doctor();
        doctor.setName(name);
        doctor.setEmail(email);
        doctor.setPassword(password);
        doctor.setSpecialty(specialty);
        doctor.setPhone(phone);
        doctor.setCreatedAt(LocalDateTime.now());

        doctorRepository.save(doctor);

        return "Doctor Registered Successfully";
    }

    // ================= LOGIN DOCTOR =================
    public String loginDoctor(String email, String password) {

        Doctor doctor = doctorRepository.findByEmail(email).orElse(null);

        if (doctor == null) {
            return "Doctor not found";
        }

        if (!doctor.getPassword().equals(password)) {
            return "Invalid Password";
        }

        return "Login Successful";
    }

    // ================= GET QUEUE =================
    public List<Queue> getQueue() {
        return queueRepository.findByStatusInOrderByTokenNumberAsc(
                List.of("WAITING", "IN_PROGRESS"));
    }

    // ================= DASHBOARD STATS =================
    public DashboardResponse getDashboardStats() {

        long totalAppointments = appointmentRepository.count();
        long pendingAppointments = appointmentRepository.countByStatus("PENDING");
        long acceptedAppointments = appointmentRepository.countByStatus("ACCEPTED");
        long completedAppointments = appointmentRepository.countByStatus("COMPLETED");
        long cancelledAppointments = appointmentRepository.countByStatus("CANCELLED");

        long waiting = queueRepository.countByStatus("WAITING");
        long inProgress = queueRepository.countByStatus("IN_PROGRESS");

        long patientsInQueue = waiting + inProgress;

        return new DashboardResponse(
                totalAppointments,
                pendingAppointments,
                acceptedAppointments,
                completedAppointments,
                cancelledAppointments,
                patientsInQueue);
    }

    // ================= CALL NEXT PATIENT =================
    public String callNextPatient() {

        Optional<Queue> currentPatient =
                queueRepository.findByStatus("IN_PROGRESS");

        if (currentPatient.isPresent()) {
            return "Patient already in consultation";
        }

        Optional<Queue> nextPatient =
                queueRepository.findFirstByStatusOrderByTokenNumberAsc("WAITING");

        if (nextPatient.isEmpty()) {
            return "No patients waiting";
        }

        Queue queue = nextPatient.get();
        queue.setStatus("IN_PROGRESS");
        queueRepository.save(queue);

        return "Patient moved to consultation";
    }

    // ================= COMPLETE CONSULTATION =================
    public String completeConsultation(Long queueId) {

        Queue queue = queueRepository.findById(queueId).orElse(null);

        if (queue == null) {
            return "Queue record not found";
        }

        if (!"IN_PROGRESS".equals(queue.getStatus())) {
            return "Only IN_PROGRESS patient can be completed";
        }

        queue.setStatus("DONE");
        queueRepository.save(queue);

        Appointment appointment = appointmentRepository
                .findById(queue.getAppointmentId())
                .orElse(null);

        if (appointment != null) {
            appointment.setStatus("COMPLETED");
            appointmentRepository.save(appointment);
        }

        return "Consultation completed successfully";
    }

    // ================= QUEUE STATS =================
    public Map<String, Long> getQueueStats() {

        Map<String, Long> stats = new HashMap<>();

        stats.put("waiting", queueRepository.countByStatus("WAITING"));
        stats.put("inProgress", queueRepository.countByStatus("IN_PROGRESS"));
        stats.put("completed", queueRepository.countByStatus("DONE"));

        return stats;
    }

    // ================= GET PROFILE (BY EMAIL) =================
    public DoctorProfileResponse getDoctorProfileByEmail(String email) {

        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found with email: " + email));

        DoctorProfileResponse response = new DoctorProfileResponse();

        response.setDoctorId(doctor.getId());
        response.setName(doctor.getName());
        response.setEmail(doctor.getEmail());
        response.setPhoneNumber(doctor.getPhone());
        response.setSpecialization(doctor.getSpecialty());

        response.setQualification(doctor.getQualification());
        response.setExperienceYears(doctor.getExperienceYears());
        response.setConsultationFee(doctor.getConsultationFee());
        response.setClinicName(doctor.getClinicName());
        response.setClinicAddress(doctor.getClinicAddress());
        response.setWorkingHours(doctor.getWorkingHours());
        response.setBio(doctor.getBio());

        return response;
    }

    // ================= UPDATE PROFILE (BY EMAIL) =================
    public DoctorProfileResponse updateDoctorProfileByEmail(
            String email,
            DoctorUpdateRequest request) {

        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctor.setName(request.getName());
        doctor.setPhone(request.getPhoneNumber());
        doctor.setSpecialty(request.getSpecialization());

        doctor.setQualification(request.getQualification());
        doctor.setExperienceYears(request.getExperienceYears());
        doctor.setConsultationFee(request.getConsultationFee());

        doctor.setClinicName(request.getClinicName());
        doctor.setClinicAddress(request.getClinicAddress());
        doctor.setWorkingHours(request.getWorkingHours());
        doctor.setBio(request.getBio());

        doctorRepository.save(doctor);

        return getDoctorProfileByEmail(email);
    }

    // ================= ANALYTICS =================
    public AnalyticsResponse getAnalytics() {

        long totalAppointments = appointmentRepository.count();
        long pendingAppointments = appointmentRepository.countByStatus("PENDING");
        long acceptedAppointments = appointmentRepository.countByStatus("ACCEPTED");
        long completedAppointments = appointmentRepository.countByStatus("COMPLETED");
        long cancelledAppointments = appointmentRepository.countByStatus("CANCELLED");

        long waitingPatients = queueRepository.countByStatus("WAITING");
        long inProgressPatients = queueRepository.countByStatus("IN_PROGRESS");
        long completedConsultations = queueRepository.countByStatus("DONE");

        long totalQueueEntries = queueRepository.count();

        return new AnalyticsResponse(
                totalAppointments,
                pendingAppointments,
                acceptedAppointments,
                completedAppointments,
                cancelledAppointments,
                waitingPatients,
                inProgressPatients,
                completedConsultations,
                totalQueueEntries
        );
    }
}