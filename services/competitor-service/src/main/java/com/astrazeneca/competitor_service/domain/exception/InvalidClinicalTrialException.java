package com.astrazeneca.competitor_service.domain.exception;

/**
 * Exception thrown when clinical trial data is invalid
 * Domain Layer - Pure Java (No framework dependencies)
 */
public class InvalidClinicalTrialException extends DomainException {

    public InvalidClinicalTrialException(String message) {
        super(message);
    }

    public InvalidClinicalTrialException(String field, String reason) {
        super("Invalid clinical trial data - " + field + ": " + reason);
    }
}

