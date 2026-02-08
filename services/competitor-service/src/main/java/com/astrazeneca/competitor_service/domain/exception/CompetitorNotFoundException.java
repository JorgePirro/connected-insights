package com.astrazeneca.competitor_service.domain.exception;

/**
 * Exception thrown when a competitor is not found
 * Domain Layer - Pure Java (No framework dependencies)
 */
public class CompetitorNotFoundException extends DomainException {

    public CompetitorNotFoundException(Long id) {
        super("Competitor not found with id: " + id);
    }

    public CompetitorNotFoundException(String message) {
        super(message);
    }
}

