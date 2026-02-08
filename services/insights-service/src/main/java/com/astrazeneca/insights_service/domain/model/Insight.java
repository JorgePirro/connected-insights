package com.astrazeneca.insights_service.domain.model;

import java.util.UUID;

public class Insight {

    private UUID id;
    private String description;
    private String therapeuticArea;
    private UUID competitorId;
    private ImpactLevel impactLevel;
    private Integer relevanceScore;

    public Insight() {
    }

    public Insight(UUID id, String description, String therapeuticArea, UUID competitorId, ImpactLevel impactLevel) {
        this.id = id;
        this.description = description;
        this.therapeuticArea = therapeuticArea;
        this.competitorId = competitorId;
        this.impactLevel = impactLevel;
        this.relevanceScore = calculateRelevanceScore(impactLevel);
    }

    public static Insight create(String description, String therapeuticArea, UUID competitorId, ImpactLevel impactLevel) {
        return new Insight(UUID.randomUUID(), description, therapeuticArea, competitorId, impactLevel);
    }

    public void update(String description, String therapeuticArea, UUID competitorId, ImpactLevel impactLevel) {
        this.description = description;
        this.therapeuticArea = therapeuticArea;
        this.competitorId = competitorId;
        this.impactLevel = impactLevel;
        this.relevanceScore = calculateRelevanceScore(impactLevel);
    }

    private Integer calculateRelevanceScore(ImpactLevel impactLevel) {
        if (impactLevel == null) {
            return null;
        }
        return impactLevel.getScore();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTherapeuticArea() {
        return therapeuticArea;
    }

    public void setTherapeuticArea(String therapeuticArea) {
        this.therapeuticArea = therapeuticArea;
    }

    public UUID getCompetitorId() {
        return competitorId;
    }

    public void setCompetitorId(UUID competitorId) {
        this.competitorId = competitorId;
    }

    public ImpactLevel getImpactLevel() {
        return impactLevel;
    }

    public void setImpactLevel(ImpactLevel impactLevel) {
        this.impactLevel = impactLevel;
        this.relevanceScore = calculateRelevanceScore(impactLevel);
    }

    public Integer getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(Integer relevanceScore) {
        this.relevanceScore = relevanceScore;
    }
}

