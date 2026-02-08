package com.astrazeneca.competitor_service.domain.exception;

/**
 * Base exception for domain-level errors
 * Domain Layer - Pure Java (No framework dependencies)
 */
public abstract class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

