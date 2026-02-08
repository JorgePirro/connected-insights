package com.astrazeneca.notification_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    private UUID id;
    private String userEmail;
    private List<String> notificationPreferences = new ArrayList<>();

    public static Subscription create(String userEmail, List<String> notificationPreferences) {
        return new Subscription(UUID.randomUUID(), userEmail, notificationPreferences != null ? notificationPreferences : new ArrayList<>());
    }

    public void updatePreferences(List<String> newPreferences) {
        this.notificationPreferences = newPreferences != null ? newPreferences : new ArrayList<>();
    }
}

