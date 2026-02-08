package com.astrazeneca.competitor_service.domain.exception;

/**
 * Exception thrown when competitor data is invalid
 * Domain Layer - Pure Java (No framework dependencies)
 */
public class InvalidCompetitorDataException extends DomainException {

    public InvalidCompetitorDataException(String message) {
        super(message);
    }

    public InvalidCompetitorDataException(String field, String reason) {
        super("Invalid competitor data - " + field + ": " + reason);
    }
}

