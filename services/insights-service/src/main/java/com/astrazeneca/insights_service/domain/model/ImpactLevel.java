package com.astrazeneca.insights_service.domain.model;

public enum ImpactLevel {
    HIGH(9),
    MEDIUM(6),
    LOW(3);

    private final int score;

    ImpactLevel(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}

