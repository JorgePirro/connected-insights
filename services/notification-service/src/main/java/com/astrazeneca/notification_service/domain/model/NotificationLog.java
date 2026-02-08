package com.astrazeneca.notification_service.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationLog {

    private UUID id;
    private String recipientEmail;
    private String messageContent;
    private LocalDateTime timestamp;
    private NotificationStatus status;

    public NotificationLog() {
    }

    public NotificationLog(UUID id, String recipientEmail, String messageContent, LocalDateTime timestamp, NotificationStatus status) {
        this.id = id;
        this.recipientEmail = recipientEmail;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
        this.status = status;
    }

    public static NotificationLog create(String recipientEmail, String messageContent) {
        return new NotificationLog(
                UUID.randomUUID(),
                recipientEmail,
                messageContent,
                LocalDateTime.now(),
                NotificationStatus.SENT
        );
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }
}

