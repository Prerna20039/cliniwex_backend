package com.clinic.backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.clinic.backend.repository.QueueRepository;

@Component
public class DailyQueueResetScheduler {

    private final QueueRepository queueRepository;

    public DailyQueueResetScheduler(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    // Runs every day at 12:00 AM (midnight)
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetQueueDaily() {

        // Reset queue for new day
        queueRepository.resetQueue();

        System.out.println("🔁 Queue reset for new day");
    }
}