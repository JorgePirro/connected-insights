package com.astrazeneca.notification_service.application.service;

import com.astrazeneca.notification_service.domain.model.NotificationLog;
import com.astrazeneca.notification_service.domain.model.Subscription;
import com.astrazeneca.notification_service.domain.ports.in.ManageSubscriptionUseCase;
import com.astrazeneca.notification_service.domain.ports.in.SendNotificationUseCase;
import com.astrazeneca.notification_service.domain.ports.out.NotificationLogRepositoryPort;
import com.astrazeneca.notification_service.domain.ports.out.NotificationSenderPort;
import com.astrazeneca.notification_service.domain.ports.out.SubscriptionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationService implements ManageSubscriptionUseCase, SendNotificationUseCase {

    private final SubscriptionRepositoryPort subscriptionRepositoryPort;
    private final NotificationLogRepositoryPort notificationLogRepositoryPort;
    private final NotificationSenderPort notificationSenderPort;

    public NotificationService(SubscriptionRepositoryPort subscriptionRepositoryPort,
                               NotificationLogRepositoryPort notificationLogRepositoryPort,
                               NotificationSenderPort notificationSenderPort) {
        this.subscriptionRepositoryPort = subscriptionRepositoryPort;
        this.notificationLogRepositoryPort = notificationLogRepositoryPort;
        this.notificationSenderPort = notificationSenderPort;
    }

    @Override
    public Subscription createSubscription(String userEmail, List<String> notificationPreferences) {
        Subscription subscription = Subscription.create(userEmail, notificationPreferences);
        return subscriptionRepositoryPort.save(subscription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subscription> listAllSubscriptions() {
        return subscriptionRepositoryPort.findAll();
    }

    @Override
    public NotificationLog sendNotification(String recipientEmail, String messageContent) {
        // First, send the notification via the sender port (mocked to console)
        notificationSenderPort.send(recipientEmail, messageContent);

        // Then, create and save the notification log
        NotificationLog notificationLog = NotificationLog.create(recipientEmail, messageContent);
        return notificationLogRepositoryPort.save(notificationLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationLog> listNotificationHistory() {
        return notificationLogRepositoryPort.findAll();
    }
}

