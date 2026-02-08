package com.astrazeneca.insights_service.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "insights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsightEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "therapeutic_area")
    private String therapeuticArea;

    @Column(name = "competitor_id")
    private UUID competitorId;

    @Column(name = "impact_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private ImpactLevelEntity impactLevel;

    @Column(name = "relevance_score")
    private Integer relevanceScore;

    public enum ImpactLevelEntity {
        HIGH, MEDIUM, LOW
    }
}

