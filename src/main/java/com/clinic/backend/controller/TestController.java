package com.clinic.backend.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.clinic.backend.dto.Patient.StatsResponse;

@RestController
@RequestMapping("/test")
public class TestController {

    private final SimpMessagingTemplate messagingTemplate;

    public TestController(
            SimpMessagingTemplate messagingTemplate) {

        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/send/{patientId}")
    public String sendTest(
            @PathVariable Long patientId) {

        StatsResponse stats = new StatsResponse();

        stats.setYourToken(999);
        stats.setCurrentToken(555);
        stats.setPatientsAhead(3);
        stats.setEstimatedWaitMinutes(30);

        System.out.println(
                "Sending test message to patient "
                        + patientId);

        messagingTemplate.convertAndSend(
                "/topic/stats/" + patientId,
                stats);

        return "sent";
    }
}