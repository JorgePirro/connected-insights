package com.astrazeneca.competitor_service.infrastructure.persistence.repository;

import com.astrazeneca.competitor_service.infrastructure.persistence.entity.CompetitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository - Infrastructure Layer
 */
@Repository
public interface JpaCompetitorRepository extends JpaRepository<CompetitorEntity, Long> {

    @Query("SELECT c FROM CompetitorEntity c LEFT JOIN FETCH c.clinicalTrials WHERE c.id = :id")
    Optional<CompetitorEntity> findByIdWithTrials(@Param("id") Long id);
}

