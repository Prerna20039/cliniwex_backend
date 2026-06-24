package com.clinic.backend.controller;

import com.clinic.backend.entity.AgentRequest;
import com.clinic.backend.repository.AgentRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @Autowired
    private AgentRequestRepository agentRepo;

    @PostMapping("/join-when-live")
    public String createRequest(@RequestBody AgentRequest req) {

        req.setStatus("PENDING");
        req.setCreatedAt(LocalDateTime.now());

        agentRepo.save(req);

        return "Request saved. You will be auto-booked when doctor goes live.";
    }
}