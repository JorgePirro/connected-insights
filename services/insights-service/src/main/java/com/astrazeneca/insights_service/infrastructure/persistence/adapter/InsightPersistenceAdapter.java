package com.astrazeneca.insights_service.infrastructure.persistence.adapter;

import com.astrazeneca.insights_service.domain.model.Insight;
import com.astrazeneca.insights_service.domain.ports.out.InsightRepositoryPort;
import com.astrazeneca.insights_service.infrastructure.persistence.entity.InsightEntity;
import com.astrazeneca.insights_service.infrastructure.persistence.mapper.PersistenceMapper;
import com.astrazeneca.insights_service.infrastructure.persistence.repository.JpaInsightRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class InsightPersistenceAdapter implements InsightRepositoryPort {

    private final JpaInsightRepository jpaRepository;
    private final PersistenceMapper mapper;

    public InsightPersistenceAdapter(JpaInsightRepository jpaRepository, PersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Insight save(Insight insight) {
        InsightEntity entity = mapper.toEntity(insight);
        InsightEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Insight> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Insight> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Insight> findByTherapeuticArea(String therapeuticArea) {
        return jpaRepository.findByTherapeuticAreaIgnoreCase(therapeuticArea).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }
}

