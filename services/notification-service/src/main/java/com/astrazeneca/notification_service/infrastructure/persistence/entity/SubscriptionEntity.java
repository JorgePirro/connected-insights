package com.astrazeneca.notification_service.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "subscription_preferences", joinColumns = @JoinColumn(name = "subscription_id"))
    @Column(name = "preference")
    private List<String> notificationPreferences = new ArrayList<>();
}
