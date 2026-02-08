package com.astrazeneca.notification_service.infrastructure.persistence.adapter;

import com.astrazeneca.notification_service.domain.model.NotificationLog;
import com.astrazeneca.notification_service.domain.ports.out.NotificationLogRepositoryPort;
import com.astrazeneca.notification_service.infrastructure.persistence.entity.NotificationLogEntity;
import com.astrazeneca.notification_service.infrastructure.persistence.mapper.PersistenceMapper;
import com.astrazeneca.notification_service.infrastructure.persistence.repository.JpaNotificationLogRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class NotificationLogPersistenceAdapter implements NotificationLogRepositoryPort {

    private final JpaNotificationLogRepository jpaNotificationLogRepository;
    private final PersistenceMapper persistenceMapper;

    public NotificationLogPersistenceAdapter(JpaNotificationLogRepository jpaNotificationLogRepository,
                                             PersistenceMapper persistenceMapper) {
        this.jpaNotificationLogRepository = jpaNotificationLogRepository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public NotificationLog save(NotificationLog notificationLog) {
        NotificationLogEntity entity = persistenceMapper.toEntity(notificationLog);
        NotificationLogEntity savedEntity = jpaNotificationLogRepository.save(entity);
        return persistenceMapper.toDomain(savedEntity);
    }

    @Override
    public List<NotificationLog> findAll() {
        List<NotificationLogEntity> entities = jpaNotificationLogRepository.findAll();
        return persistenceMapper.toNotificationLogDomainList(entities);
    }

    @Override
    public Optional<NotificationLog> findById(UUID id) {
        return jpaNotificationLogRepository.findById(id)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public List<NotificationLog> findByRecipientEmail(String recipientEmail) {
        List<NotificationLogEntity> entities = jpaNotificationLogRepository.findByRecipientEmail(recipientEmail);
        return persistenceMapper.toNotificationLogDomainList(entities);
    }
}

