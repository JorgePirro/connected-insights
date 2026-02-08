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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Notification Service", description = "APIs for managing subscriptions and sending notifications")
public class NotificationController {

    private final ManageSubscriptionUseCase manageSubscriptionUseCase;
    private final SendNotificationUseCase sendNotificationUseCase;
    private final WebMapper webMapper;

    public NotificationController(ManageSubscriptionUseCase manageSubscriptionUseCase,
                                  SendNotificationUseCase sendNotificationUseCase,
                                  WebMapper webMapper) {
        this.manageSubscriptionUseCase = manageSubscriptionUseCase;
        this.sendNotificationUseCase = sendNotificationUseCase;
        this.webMapper = webMapper;
    }

    @Operation(summary = "Create a new subscription", description = "Creates a new user subscription with notification preferences")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/subscriptions")
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Valid @RequestBody CreateSubscriptionRequest request) {
        Subscription subscription = manageSubscriptionUseCase.createSubscription(
                request.getUserEmail(),
                request.getNotificationPreferences()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(webMapper.toResponse(subscription));
    }

    @Operation(summary = "List all subscriptions", description = "Retrieves all active subscriptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved subscriptions")
    })
    @GetMapping("/subscriptions")
    public ResponseEntity<List<SubscriptionResponse>> listSubscriptions() {
        List<Subscription> subscriptions = manageSubscriptionUseCase.listAllSubscriptions();
        return ResponseEntity.ok(webMapper.toSubscriptionResponseList(subscriptions));
    }

    @Operation(summary = "Send a notification", description = "Sends a notification to a recipient and logs it to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/notifications/send")
    public ResponseEntity<NotificationLogResponse> sendNotification(
            @Valid @RequestBody SendNotificationRequest request) {
        NotificationLog notificationLog = sendNotificationUseCase.sendNotification(
                request.getRecipientEmail(),
                request.getMessageContent()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(webMapper.toResponse(notificationLog));
    }

    @Operation(summary = "List notification history", description = "Retrieves the history of all sent notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved notification history")
    })
    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationLogResponse>> listNotificationHistory() {
        List<NotificationLog> notificationLogs = sendNotificationUseCase.listNotificationHistory();
        return ResponseEntity.ok(webMapper.toNotificationLogResponseList(notificationLogs));
    }
}
