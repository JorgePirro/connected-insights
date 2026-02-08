package com.astrazeneca.notification_service.infrastructure.notification.adapter;

import com.astrazeneca.notification_service.domain.ports.out.NotificationSenderPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ConsoleNotificationAdapter implements NotificationSenderPort {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void send(String recipientEmail, String messageContent) {
        String timestamp = LocalDateTime.now().format(FORMATTER);

        System.out.println("========================================");
        System.out.println("📧 NOTIFICATION SENT");
        System.out.println("========================================");
        System.out.println("Timestamp: " + timestamp);
        System.out.println("To: " + recipientEmail);
        System.out.println("Message: " + messageContent);
        System.out.println("Status: SENT");
        System.out.println("========================================");
    }
}

