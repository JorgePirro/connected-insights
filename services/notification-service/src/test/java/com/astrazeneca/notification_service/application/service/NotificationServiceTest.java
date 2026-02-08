package com.astrazeneca.notification_service.application.service;

import com.astrazeneca.notification_service.domain.model.NotificationLog;
import com.astrazeneca.notification_service.domain.model.Subscription;
import com.astrazeneca.notification_service.domain.ports.out.NotificationLogRepositoryPort;
import com.astrazeneca.notification_service.domain.ports.out.NotificationSenderPort;
import com.astrazeneca.notification_service.domain.ports.out.SubscriptionRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationService Tests")
class NotificationServiceTest {

    @Mock
    private SubscriptionRepositoryPort subscriptionRepositoryPort;

    @Mock
    private NotificationLogRepositoryPort notificationLogRepositoryPort;

    @Mock
    private NotificationSenderPort notificationSenderPort;

    @InjectMocks
    private NotificationService notificationService;

    @Nested
    @DisplayName("Subscription Management Tests")
    class SubscriptionManagementTests {

        @Test
        @DisplayName("Should create subscription successfully")
        void shouldCreateSubscription() {
            List<String> preferences = List.of("email", "sms");
            when(subscriptionRepositoryPort.save(any(Subscription.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Subscription result = notificationService.createSubscription("test@example.com", preferences);

            assertNotNull(result);
            assertEquals("test@example.com", result.getUserEmail());
            assertEquals(preferences, result.getNotificationPreferences());
            verify(subscriptionRepositoryPort).save(any(Subscription.class));
        }

        @Test
        @DisplayName("Should list all subscriptions")
        void shouldListAllSubscriptions() {
            Subscription subscription = Subscription.create("user@example.com", List.of("email"));
            when(subscriptionRepositoryPort.findAll()).thenReturn(List.of(subscription));

            List<Subscription> result = notificationService.listAllSubscriptions();

            assertEquals(1, result.size());
            verify(subscriptionRepositoryPort).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no subscriptions")
        void shouldReturnEmptyListWhenNoSubscriptions() {
            when(subscriptionRepositoryPort.findAll()).thenReturn(Collections.emptyList());

            List<Subscription> result = notificationService.listAllSubscriptions();

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Notification Sending Tests")
    class NotificationSendingTests {

        @Test
        @DisplayName("Should send notification and save log")
        void shouldSendNotificationAndSaveLog() {
            when(notificationLogRepositoryPort.save(any(NotificationLog.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            NotificationLog result = notificationService.sendNotification("test@example.com", "Hello!");

            verify(notificationSenderPort).send("test@example.com", "Hello!");
            verify(notificationLogRepositoryPort).save(any(NotificationLog.class));
            assertNotNull(result);
            assertEquals("test@example.com", result.getRecipientEmail());
            assertEquals("Hello!", result.getMessageContent());
        }

        @Test
        @DisplayName("Should list notification history")
        void shouldListNotificationHistory() {
            NotificationLog log = NotificationLog.create("user@example.com", "Test message");
            when(notificationLogRepositoryPort.findAll()).thenReturn(List.of(log));

            List<NotificationLog> result = notificationService.listNotificationHistory();

            assertEquals(1, result.size());
            verify(notificationLogRepositoryPort).findAll();
        }
    }
}
