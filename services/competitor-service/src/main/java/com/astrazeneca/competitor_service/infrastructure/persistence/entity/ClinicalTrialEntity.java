package com.astrazeneca.competitor_service.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA Entity - Infrastructure Layer
 * Contains JPA annotations for persistence
 */
@Entity
@Table(name = "clinical_trials")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalTrialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trial_id", nullable = false)
    private String trialId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phase;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String indication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competitor_id")
    private CompetitorEntity competitor;
}

