package com.clinic.backend.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.backend.Entity.Queue;
import com.clinic.backend.Service.QueueService;

@RestController
@RequestMapping("/api/doctor/queue")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping
    public List<Queue> getQueue() {
        return queueService.getQueue();
    }

    @PostMapping("/call-next")
    public Queue callNextPatient() {
        return queueService.callNextPatient();
    }

    @PostMapping("/complete/{appointmentId}")
    public Queue completeConsultation(
            @PathVariable Long appointmentId) {

        return queueService
                .completeConsultation(appointmentId);
    }
}