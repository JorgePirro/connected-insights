package com.astrazeneca.notification_service.infrastructure.web.controller;

import com.astrazeneca.notification_service.domain.model.NotificationLog;
import com.astrazeneca.notification_service.domain.model.Subscription;
import com.astrazeneca.notification_service.domain.ports.in.ManageSubscriptionUseCase;
import com.astrazeneca.notification_service.domain.ports.in.SendNotificationUseCase;
import com.astrazeneca.notification_service.infrastructure.web.dto.CreateSubscriptionRequest;
import com.astrazeneca.notification_service.infrastructure.web.dto.NotificationLogResponse;
import com.astrazeneca.notification_service.infrastructure.web.dto.SendNotificationRequest;
import com.astrazeneca.notification_service.infrastructure.web.dto.SubscriptionResponse;
import com.astrazeneca.notification_service.infrastructure.web.mapper.WebMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationController Tests")
class NotificationControllerTest {

    @Mock
    private ManageSubscriptionUseCase manageSubscriptionUseCase;

    @Mock
    private SendNotificationUseCase sendNotificationUseCase;

    @Mock
    private WebMapper webMapper;

    @InjectMocks
    private NotificationController notificationController;

    @Nested
    @DisplayName("Subscription Endpoints Tests")
    class SubscriptionEndpointsTests {

        @Test
        @DisplayName("Should create subscription and return 201")
        void shouldCreateSubscriptionAndReturn201() {
            CreateSubscriptionRequest request = new CreateSubscriptionRequest();
            request.setUserEmail("test@example.com");
            request.setNotificationPreferences(List.of("email", "sms"));

            Subscription subscription = new Subscription(UUID.randomUUID(), "test@example.com", List.of("email", "sms"));
            SubscriptionResponse response = new SubscriptionResponse();
            response.setUserEmail("test@example.com");

            when(manageSubscriptionUseCase.createSubscription("test@example.com", List.of("email", "sms")))
                    .thenReturn(subscription);
            when(webMapper.toResponse(subscription)).thenReturn(response);

            ResponseEntity<SubscriptionResponse> result = notificationController.createSubscription(request);

            assertEquals(HttpStatus.CREATED, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals("test@example.com", result.getBody().getUserEmail());
            verify(manageSubscriptionUseCase).createSubscription("test@example.com", List.of("email", "sms"));
        }

        @Test
        @DisplayName("Should list all subscriptions and return 200")
        void shouldListSubscriptionsAndReturn200() {
            List<Subscription> subscriptions = List.of(
                    new Subscription(UUID.randomUUID(), "user1@example.com", List.of("email"))
            );
            List<SubscriptionResponse> responses = List.of(new SubscriptionResponse());

            when(manageSubscriptionUseCase.listAllSubscriptions()).thenReturn(subscriptions);
            when(webMapper.toSubscriptionResponseList(subscriptions)).thenReturn(responses);

            ResponseEntity<List<SubscriptionResponse>> result = notificationController.listSubscriptions();

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(1, result.getBody().size());
            verify(manageSubscriptionUseCase).listAllSubscriptions();
        }

        @Test
        @DisplayName("Should return empty list when no subscriptions")
        void shouldReturnEmptyListWhenNoSubscriptions() {
            when(manageSubscriptionUseCase.listAllSubscriptions()).thenReturn(Collections.emptyList());
            when(webMapper.toSubscriptionResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

            ResponseEntity<List<SubscriptionResponse>> result = notificationController.listSubscriptions();

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Nested
    @DisplayName("Notification Endpoints Tests")
    class NotificationEndpointsTests {

        @Test
        @DisplayName("Should send notification and return 201")
        void shouldSendNotificationAndReturn201() {
            SendNotificationRequest request = new SendNotificationRequest();
            request.setRecipientEmail("recipient@example.com");
            request.setMessageContent("Hello!");

            NotificationLog log = new NotificationLog(UUID.randomUUID(), "recipient@example.com", "Hello!", LocalDateTime.now(), null);
            NotificationLogResponse response = new NotificationLogResponse();
            response.setRecipientEmail("recipient@example.com");

            when(sendNotificationUseCase.sendNotification("recipient@example.com", "Hello!"))
                    .thenReturn(log);
            when(webMapper.toResponse(log)).thenReturn(response);

            ResponseEntity<NotificationLogResponse> result = notificationController.sendNotification(request);

            assertEquals(HttpStatus.CREATED, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals("recipient@example.com", result.getBody().getRecipientEmail());
            verify(sendNotificationUseCase).sendNotification("recipient@example.com", "Hello!");
        }

        @Test
        @DisplayName("Should list notification history and return 200")
        void shouldListNotificationHistoryAndReturn200() {
            List<NotificationLog> logs = List.of(
                    new NotificationLog(UUID.randomUUID(), "user@example.com", "Test", LocalDateTime.now(), null)
            );
            List<NotificationLogResponse> responses = List.of(new NotificationLogResponse());

            when(sendNotificationUseCase.listNotificationHistory()).thenReturn(logs);
            when(webMapper.toNotificationLogResponseList(logs)).thenReturn(responses);

            ResponseEntity<List<NotificationLogResponse>> result = notificationController.listNotificationHistory();

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(1, result.getBody().size());
            verify(sendNotificationUseCase).listNotificationHistory();
        }
    }
}