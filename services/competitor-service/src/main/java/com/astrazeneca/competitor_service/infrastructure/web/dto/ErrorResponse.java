package com.astrazeneca.competitor_service.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response DTO
 * Infrastructure/Web Layer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Error response object")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Error type", example = "NOT_FOUND")
    private String error;

    @Schema(description = "Error message", example = "Competitor not found with id: 1")
    private String message;

    @Schema(description = "Request path", example = "/api/competitors/1")
    private String path;

    @Schema(description = "Timestamp of the error")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "List of validation errors (if applicable)")
    private List<ValidationError> validationErrors;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Validation error details")
    public static class ValidationError {

        @Schema(description = "Field that failed validation", example = "name")
        private String field;

        @Schema(description = "Validation error message", example = "must not be blank")
        private String message;

        @Schema(description = "Rejected value", example = "")
        private Object rejectedValue;
    }
}

