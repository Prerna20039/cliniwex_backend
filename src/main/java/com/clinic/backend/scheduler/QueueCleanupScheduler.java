package com.clinic.backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.clinic.backend.repository.QueueRepository;

@Component
public class QueueCleanupScheduler {

    private final QueueRepository queueRepository;

    public QueueCleanupScheduler(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    // Runs every day at 2:00 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteOldData() {

        int deleted = queueRepository.deleteQueueOlderThan90Days();

        System.out.println("✅ Deleted records older than 90 days: " + deleted);
    }
}