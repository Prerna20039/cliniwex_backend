package com.clinic.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.backend.entity.Queue;
import com.clinic.backend.service.QueueService;
import com.clinic.backend.dto.Patient.QueueResponse;

@RestController
@RequestMapping("/api/queue")
public class QueueController {
    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping("/status/{appointmentId}")
    public QueueResponse getQueueStatus(
            @PathVariable Long appointmentId) {

        return queueService.getQueueStatus(appointmentId);
    }
}