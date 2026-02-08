package com.astrazeneca.competitor_service.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity - Infrastructure Layer
 * Contains JPA annotations for persistence
 */
@Entity
@Table(name = "competitors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetitorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "competitor_therapeutic_areas", joinColumns = @JoinColumn(name = "competitor_id"))
    @Column(name = "therapeutic_area")
    private List<String> therapeuticAreas = new ArrayList<>();

    @Column(nullable = false)
    private String headquarters;

    @OneToMany(mappedBy = "competitor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ClinicalTrialEntity> clinicalTrials = new ArrayList<>();
}
