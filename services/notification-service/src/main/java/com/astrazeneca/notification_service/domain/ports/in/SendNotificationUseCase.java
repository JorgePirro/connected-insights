package com.astrazeneca.notification_service.domain.ports.in;

import com.astrazeneca.notification_service.domain.model.NotificationLog;

import java.util.List;

public interface SendNotificationUseCase {

    NotificationLog sendNotification(String recipientEmail, String messageContent);

    List<NotificationLog> listNotificationHistory();
}

