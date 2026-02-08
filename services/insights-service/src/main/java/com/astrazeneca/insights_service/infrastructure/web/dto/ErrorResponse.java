package com.astrazeneca.insights_service.infrastructure.web.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp
) {}

