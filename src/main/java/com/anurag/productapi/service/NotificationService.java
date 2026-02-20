package com.anurag.productapi.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Async
    public void sendWelcomeEmailAsync(String username) {
        // Simulating a time-consuming background task
        System.out.println("Async Process Started: Sending welcome email to " + username);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Async Process Completed: Email sent to " + username);
    }
}