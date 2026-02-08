package com.astrazeneca.competitor_service.infrastructure.web.exception;

import com.astrazeneca.competitor_service.domain.exception.CompetitorNotFoundException;
import com.astrazeneca.competitor_service.domain.exception.InvalidClinicalTrialException;
import com.astrazeneca.competitor_service.domain.exception.InvalidCompetitorDataException;
import com.astrazeneca.competitor_service.infrastructure.web.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/api/competitors/1");
    }

    // ==================== Domain Exceptions ====================

    @Test
    @DisplayName("Should handle CompetitorNotFoundException with 404 status")
    void shouldHandleCompetitorNotFoundException() {
        // Given
        Long competitorId = 999L;
        CompetitorNotFoundException exception = new CompetitorNotFoundException(competitorId);

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleCompetitorNotFound(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getError()).isEqualTo("NOT_FOUND");
        assertThat(response.getBody().getMessage()).contains("999");
        assertThat(response.getBody().getPath()).isEqualTo("/api/competitors/1");
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should handle CompetitorNotFoundException with custom message")
    void shouldHandleCompetitorNotFoundExceptionWithCustomMessage() {
        // Given
        CompetitorNotFoundException exception = new CompetitorNotFoundException("Custom not found message");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleCompetitorNotFound(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Custom not found message");
    }

    @Test
    @DisplayName("Should handle InvalidCompetitorDataException with 400 status")
    void shouldHandleInvalidCompetitorDataException() {
        // Given
        InvalidCompetitorDataException exception = new InvalidCompetitorDataException("Name cannot be blank");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInvalidCompetitorData(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("INVALID_COMPETITOR_DATA");
        assertThat(response.getBody().getMessage()).isEqualTo("Name cannot be blank");
        assertThat(response.getBody().getPath()).isEqualTo("/api/competitors/1");
    }

    @Test
    @DisplayName("Should handle InvalidCompetitorDataException with field and reason")
    void shouldHandleInvalidCompetitorDataExceptionWithFieldAndReason() {
        // Given
        InvalidCompetitorDataException exception = new InvalidCompetitorDataException("name", "must not be blank");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInvalidCompetitorData(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("name");
        assertThat(response.getBody().getMessage()).contains("must not be blank");
    }

    @Test
    @DisplayName("Should handle InvalidClinicalTrialException with 400 status")
    void shouldHandleInvalidClinicalTrialException() {
        // Given
        InvalidClinicalTrialException exception = new InvalidClinicalTrialException("Trial ID cannot be blank");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInvalidClinicalTrial(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("INVALID_CLINICAL_TRIAL_DATA");
        assertThat(response.getBody().getMessage()).isEqualTo("Trial ID cannot be blank");
        assertThat(response.getBody().getPath()).isEqualTo("/api/competitors/1");
    }

    @Test
    @DisplayName("Should handle InvalidClinicalTrialException with field and reason")
    void shouldHandleInvalidClinicalTrialExceptionWithFieldAndReason() {
        // Given
        InvalidClinicalTrialException exception = new InvalidClinicalTrialException("trialId", "must not be blank");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInvalidClinicalTrial(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("trialId");
        assertThat(response.getBody().getMessage()).contains("must not be blank");
    }

    // ==================== Validation Exceptions ====================

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with validation errors")
    void shouldHandleMethodArgumentNotValidException() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("competitor", "name", "invalid", false,
                null, null, "Name must not be blank");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationErrors(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed for one or more fields");
        assertThat(response.getBody().getValidationErrors()).hasSize(1);
        assertThat(response.getBody().getValidationErrors().getFirst().getField()).isEqualTo("name");
        assertThat(response.getBody().getValidationErrors().getFirst().getMessage()).isEqualTo("Name must not be blank");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with multiple validation errors")
    void shouldHandleMethodArgumentNotValidExceptionWithMultipleErrors() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("competitor", "name", null, false,
                null, null, "Name must not be blank");
        FieldError fieldError2 = new FieldError("competitor", "headquarters", null, false,
                null, null, "Headquarters must not be blank");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationErrors(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getValidationErrors()).hasSize(2);
    }

    @Test
    @DisplayName("Should handle MethodArgumentTypeMismatchException with 400 status")
    void shouldHandleMethodArgumentTypeMismatchException() {
        // Given
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                "abc", Long.class, "id", null, new IllegalArgumentException("Invalid type"));

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTypeMismatch(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("TYPE_MISMATCH");
        assertThat(response.getBody().getMessage()).contains("id");
        assertThat(response.getBody().getMessage()).contains("Long");
    }

    // ==================== Request Exceptions ====================

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException with 400 status")
    void shouldHandleHttpMessageNotReadableException() {
        // Given
        HttpInputMessage httpInputMessage = new HttpInputMessage() {
            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream(new byte[0]);
            }

            @Override
            public HttpHeaders getHeaders() {
                return new HttpHeaders();
            }
        };
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(
                "Malformed JSON", httpInputMessage);

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMessageNotReadable(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("MALFORMED_JSON");
        assertThat(response.getBody().getMessage()).isEqualTo("Request body is missing or malformed");
    }

    @Test
    @DisplayName("Should handle HttpMediaTypeNotSupportedException with 415 status")
    void shouldHandleHttpMediaTypeNotSupportedException() {
        // Given
        HttpMediaTypeNotSupportedException exception = new HttpMediaTypeNotSupportedException(
                MediaType.TEXT_PLAIN, List.of(MediaType.APPLICATION_JSON));

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMediaTypeNotSupported(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(415);
        assertThat(response.getBody().getError()).isEqualTo("UNSUPPORTED_MEDIA_TYPE");
        assertThat(response.getBody().getMessage()).contains("application/json");
    }

    @Test
    @DisplayName("Should handle HttpRequestMethodNotSupportedException with 405 status")
    void shouldHandleHttpRequestMethodNotSupportedException() {
        // Given
        HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException(
                "PUT", List.of("GET", "POST"));

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMethodNotSupported(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(405);
        assertThat(response.getBody().getError()).isEqualTo("METHOD_NOT_ALLOWED");
        assertThat(response.getBody().getMessage()).contains("PUT");
    }

    @Test
    @DisplayName("Should handle MissingServletRequestParameterException with 400 status")
    void shouldHandleMissingServletRequestParameterException() {
        // Given
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException(
                "page", "int");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMissingParameter(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("MISSING_PARAMETER");
        assertThat(response.getBody().getMessage()).contains("page");
    }

    @Test
    @DisplayName("Should handle NoHandlerFoundException with 404 status")
    void shouldHandleNoHandlerFoundException() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        NoHandlerFoundException exception = new NoHandlerFoundException(
                "GET", "/api/unknown", headers);

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleNoHandlerFound(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getError()).isEqualTo("ENDPOINT_NOT_FOUND");
        assertThat(response.getBody().getMessage()).contains("GET");
        assertThat(response.getBody().getMessage()).contains("/api/unknown");
    }

    // ==================== Generic Exception Handler ====================

    @Test
    @DisplayName("Should handle generic Exception with 500 status")
    void shouldHandleGenericException() {
        // Given
        Exception exception = new RuntimeException("Unexpected error");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getError()).isEqualTo("INTERNAL_SERVER_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred. Please try again later.");
        assertThat(response.getBody().getPath()).isEqualTo("/api/competitors/1");
    }

    @Test
    @DisplayName("Should handle NullPointerException with 500 status")
    void shouldHandleNullPointerException() {
        // Given
        NullPointerException exception = new NullPointerException("Null value encountered");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getError()).isEqualTo("INTERNAL_SERVER_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred. Please try again later.");
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException with 500 status")
    void shouldHandleIllegalArgumentException() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getError()).isEqualTo("INTERNAL_SERVER_ERROR");
    }
}

