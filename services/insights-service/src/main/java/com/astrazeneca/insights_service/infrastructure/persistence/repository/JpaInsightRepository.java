package com.astrazeneca.insights_service.infrastructure.persistence.repository;

import com.astrazeneca.insights_service.infrastructure.persistence.entity.InsightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaInsightRepository extends JpaRepository<InsightEntity, UUID> {

    List<InsightEntity> findByTherapeuticAreaIgnoreCase(String therapeuticArea);
}

