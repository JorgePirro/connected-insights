package com.astrazeneca.notification_service.infrastructure.persistence.adapter;

import com.astrazeneca.notification_service.domain.model.Subscription;
import com.astrazeneca.notification_service.domain.ports.out.SubscriptionRepositoryPort;
import com.astrazeneca.notification_service.infrastructure.persistence.entity.SubscriptionEntity;
import com.astrazeneca.notification_service.infrastructure.persistence.mapper.PersistenceMapper;
import com.astrazeneca.notification_service.infrastructure.persistence.repository.JpaSubscriptionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SubscriptionPersistenceAdapter implements SubscriptionRepositoryPort {

    private final JpaSubscriptionRepository jpaSubscriptionRepository;
    private final PersistenceMapper persistenceMapper;

    public SubscriptionPersistenceAdapter(JpaSubscriptionRepository jpaSubscriptionRepository,
                                          PersistenceMapper persistenceMapper) {
        this.jpaSubscriptionRepository = jpaSubscriptionRepository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public Subscription save(Subscription subscription) {
        SubscriptionEntity entity = persistenceMapper.toEntity(subscription);
        SubscriptionEntity savedEntity = jpaSubscriptionRepository.save(entity);
        return persistenceMapper.toDomain(savedEntity);
    }

    @Override
    public List<Subscription> findAll() {
        List<SubscriptionEntity> entities = jpaSubscriptionRepository.findAll();
        return persistenceMapper.toSubscriptionDomainList(entities);
    }

    @Override
    public Optional<Subscription> findById(UUID id) {
        return jpaSubscriptionRepository.findById(id)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public Optional<Subscription> findByUserEmail(String userEmail) {
        return jpaSubscriptionRepository.findByUserEmail(userEmail)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaSubscriptionRepository.deleteById(id);
    }
}

