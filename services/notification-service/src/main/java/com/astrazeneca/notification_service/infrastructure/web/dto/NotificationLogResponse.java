package com.astrazeneca.notification_service.infrastructure.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLogResponse {

    private UUID id;
    private String recipientEmail;
    private String messageContent;
    private LocalDateTime timestamp;
    private String status;
}

