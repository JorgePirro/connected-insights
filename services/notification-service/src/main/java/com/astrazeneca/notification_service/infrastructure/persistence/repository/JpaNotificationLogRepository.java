package com.astrazeneca.notification_service.infrastructure.persistence.repository;

import com.astrazeneca.notification_service.infrastructure.persistence.entity.NotificationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaNotificationLogRepository extends JpaRepository<NotificationLogEntity, UUID> {

    List<NotificationLogEntity> findByRecipientEmail(String recipientEmail);
}

