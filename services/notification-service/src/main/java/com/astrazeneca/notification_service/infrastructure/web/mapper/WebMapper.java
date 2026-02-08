package com.astrazeneca.notification_service.infrastructure.web.mapper;

import com.astrazeneca.notification_service.domain.model.NotificationLog;
import com.astrazeneca.notification_service.domain.model.Subscription;
import com.astrazeneca.notification_service.infrastructure.web.dto.NotificationLogResponse;
import com.astrazeneca.notification_service.infrastructure.web.dto.SubscriptionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WebMapper {

    // Subscription mappings
    SubscriptionResponse toResponse(Subscription subscription);

    List<SubscriptionResponse> toSubscriptionResponseList(List<Subscription> subscriptions);

    // NotificationLog mappings
    @Mapping(target = "status", expression = "java(notificationLog.getStatus() != null ? notificationLog.getStatus().name() : null)")
    NotificationLogResponse toResponse(NotificationLog notificationLog);

    List<NotificationLogResponse> toNotificationLogResponseList(List<NotificationLog> notificationLogs);
}

