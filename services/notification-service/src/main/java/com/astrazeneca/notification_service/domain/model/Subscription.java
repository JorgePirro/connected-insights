package com.astrazeneca.notification_service.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Subscription {

    private UUID id;
    private String userEmail;
    private List<String> notificationPreferences;

    public Subscription() {
        this.notificationPreferences = new ArrayList<>();
    }

    public Subscription(UUID id, String userEmail, List<String> notificationPreferences) {
        this.id = id;
        this.userEmail = userEmail;
        this.notificationPreferences = notificationPreferences != null ? notificationPreferences : new ArrayList<>();
    }

    public static Subscription create(String userEmail, List<String> notificationPreferences) {
        return new Subscription(UUID.randomUUID(), userEmail, notificationPreferences);
    }

    public void updatePreferences(List<String> newPreferences) {
        this.notificationPreferences = newPreferences != null ? newPreferences : new ArrayList<>();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<String> getNotificationPreferences() {
        return notificationPreferences;
    }

    public void setNotificationPreferences(List<String> notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }
}

