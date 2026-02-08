package com.astrazeneca.notification_service.infrastructure.persistence.mapper;

import com.astrazeneca.notification_service.domain.model.NotificationLog;
import com.astrazeneca.notification_service.domain.model.NotificationStatus;
import com.astrazeneca.notification_service.domain.model.Subscription;
import com.astrazeneca.notification_service.infrastructure.persistence.entity.NotificationLogEntity;
import com.astrazeneca.notification_service.infrastructure.persistence.entity.NotificationStatusEntity;
import com.astrazeneca.notification_service.infrastructure.persistence.entity.SubscriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersistenceMapper {

    // Subscription mappings
    SubscriptionEntity toEntity(Subscription subscription);

    Subscription toDomain(SubscriptionEntity entity);

    List<Subscription> toSubscriptionDomainList(List<SubscriptionEntity> entities);

    // NotificationLog mappings
    NotificationLogEntity toEntity(NotificationLog notificationLog);

    NotificationLog toDomain(NotificationLogEntity entity);

    List<NotificationLog> toNotificationLogDomainList(List<NotificationLogEntity> entities);

    // Status mappings
    default NotificationStatusEntity toEntity(NotificationStatus status) {
        if (status == null) {
            return null;
        }
        return NotificationStatusEntity.valueOf(status.name());
    }

    default NotificationStatus toDomain(NotificationStatusEntity status) {
        if (status == null) {
            return null;
        }
        return NotificationStatus.valueOf(status.name());
    }
}

