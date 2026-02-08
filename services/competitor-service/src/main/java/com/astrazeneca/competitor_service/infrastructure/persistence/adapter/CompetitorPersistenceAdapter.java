package com.astrazeneca.competitor_service.infrastructure.persistence.adapter;

import com.astrazeneca.competitor_service.domain.model.Competitor;
import com.astrazeneca.competitor_service.domain.ports.out.CompetitorRepositoryPort;
import com.astrazeneca.competitor_service.infrastructure.persistence.entity.CompetitorEntity;
import com.astrazeneca.competitor_service.infrastructure.persistence.mapper.PersistenceMapper;
import com.astrazeneca.competitor_service.infrastructure.persistence.repository.JpaCompetitorRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Persistence Adapter - Implements Output Port (Domain Repository Interface)
 * Infrastructure Layer - Adapts JPA to Domain needs
 */
@Component
public class CompetitorPersistenceAdapter implements CompetitorRepositoryPort {

    private final JpaCompetitorRepository jpaCompetitorRepository;
    private final PersistenceMapper persistenceMapper;

    public CompetitorPersistenceAdapter(JpaCompetitorRepository jpaCompetitorRepository,
                                       PersistenceMapper persistenceMapper) {
        this.jpaCompetitorRepository = jpaCompetitorRepository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public Competitor save(Competitor competitor) {
        CompetitorEntity entity = persistenceMapper.toEntity(competitor);
        CompetitorEntity savedEntity = jpaCompetitorRepository.save(entity);
        return persistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Competitor> findById(Long id) {
        return jpaCompetitorRepository.findByIdWithTrials(id)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public List<Competitor> findAll() {
        List<CompetitorEntity> entities = jpaCompetitorRepository.findAll();
        return persistenceMapper.toDomainList(entities);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaCompetitorRepository.existsById(id);
    }
}

