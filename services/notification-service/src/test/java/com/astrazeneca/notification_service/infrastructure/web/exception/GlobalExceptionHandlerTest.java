package com.astrazeneca.notification_service.infrastructure.web.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Nested
    @DisplayName("Validation Exception Tests")
    class ValidationExceptionTests {

        @Test
        @DisplayName("Should handle MethodArgumentNotValidException and return 400")
        void shouldHandleValidationException() {
            MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);
            FieldError fieldError = new FieldError("object", "email", "must not be blank");

            when(exception.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(exception);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(400, response.getBody().getStatus());
            assertEquals("Validation Failed", response.getBody().getError());
            assertTrue(response.getBody().getMessage().contains("email"));
        }

        @Test
        @DisplayName("Should handle multiple validation errors")
        void shouldHandleMultipleValidationErrors() {
            MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);
            FieldError fieldError1 = new FieldError("object", "email", "must not be blank");
            FieldError fieldError2 = new FieldError("object", "name", "size must be between 1 and 100");

            when(exception.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(exception);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertTrue(response.getBody().getMessage().contains("email"));
            assertTrue(response.getBody().getMessage().contains("name"));
        }
    }

    @Nested
    @DisplayName("ResourceNotFoundException Tests")
    class ResourceNotFoundExceptionTests {

        @Test
        @DisplayName("Should handle ResourceNotFoundException and return 404")
        void shouldHandleResourceNotFoundException() {
            ResourceNotFoundException exception = new ResourceNotFoundException("Subscription not found");

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceNotFoundException(exception);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(404, response.getBody().getStatus());
            assertEquals("Resource Not Found", response.getBody().getError());
            assertEquals("Subscription not found", response.getBody().getMessage());
        }
    }

    @Nested
    @DisplayName("DuplicateResourceException Tests")
    class DuplicateResourceExceptionTests {

        @Test
        @DisplayName("Should handle DuplicateResourceException and return 409")
        void shouldHandleDuplicateResourceException() {
            DuplicateResourceException exception = new DuplicateResourceException("Email already exists");

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDuplicateResourceException(exception);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(409, response.getBody().getStatus());
            assertEquals("Duplicate Resource", response.getBody().getError());
            assertEquals("Email already exists", response.getBody().getMessage());
        }
    }

    @Nested
    @DisplayName("Generic Exception Tests")
    class GenericExceptionTests {

        @Test
        @DisplayName("Should handle generic Exception and return 500")
        void shouldHandleGenericException() {
            Exception exception = new RuntimeException("Unexpected error occurred");

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(500, response.getBody().getStatus());
            assertEquals("Internal Server Error", response.getBody().getError());
            assertEquals("Unexpected error occurred", response.getBody().getMessage());
        }

        @Test
        @DisplayName("Should handle NullPointerException and return 500")
        void shouldHandleNullPointerException() {
            NullPointerException exception = new NullPointerException("Null value encountered");

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals(500, response.getBody().getStatus());
        }
    }
}