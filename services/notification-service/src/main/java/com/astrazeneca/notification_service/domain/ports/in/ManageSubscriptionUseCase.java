package com.astrazeneca.notification_service.domain.ports.in;

import com.astrazeneca.notification_service.domain.model.Subscription;

import java.util.List;

public interface ManageSubscriptionUseCase {

    Subscription createSubscription(String userEmail, List<String> notificationPreferences);

    List<Subscription> listAllSubscriptions();
}

