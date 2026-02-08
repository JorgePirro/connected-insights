package com.astrazeneca.notification_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLog {

    private UUID id;
    private String recipientEmail;
    private String messageContent;
    private LocalDateTime timestamp;
    private NotificationStatus status;

    public static NotificationLog create(String recipientEmail, String messageContent) {
        return new NotificationLog(
                UUID.randomUUID(),
                recipientEmail,
                messageContent,
                LocalDateTime.now(),
                NotificationStatus.SENT
        );
    }
}

