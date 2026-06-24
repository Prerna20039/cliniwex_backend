package com.clinic.backend.service;

import com.clinic.backend.dto.Patient.AppointmentRequest;
import com.clinic.backend.entity.AgentRequest;
import com.clinic.backend.entity.Doctor;
import com.clinic.backend.repository.AgentRequestRepository;
import com.clinic.backend.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class AgentService {

    @Autowired
    private AgentRequestRepository agentRepo;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorRepository doctorRepo;

    public void processDoctorLive(Long doctorId) {

        List<AgentRequest> requests =
                agentRepo.findByDoctorIdAndStatus(doctorId, "PENDING");

        for (AgentRequest req : requests) {
            try {
                AppointmentRequest appointmentRequest = new AppointmentRequest();
                appointmentRequest.setDoctorId(doctorId);
                appointmentRequest.setAppointmentDate(java.time.LocalDate.now());
                appointmentRequest.setAppointmentTime(LocalTime.now());
                

                appointmentService.bookAppointment(
                    req.getPatientId(),
                    appointmentRequest
                );

                req.setStatus("BOOKED");
                agentRepo.save(req);

                System.out.println("Agent booked for patient: " + req.getPatientId());

            } catch (Exception e) {
                req.setStatus("FAILED");
                agentRepo.save(req);
            }
        }
    }
}