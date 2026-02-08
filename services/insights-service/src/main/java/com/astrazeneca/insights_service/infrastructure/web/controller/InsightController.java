package com.astrazeneca.insights_service.infrastructure.web.controller;

import com.astrazeneca.insights_service.domain.model.Insight;
import com.astrazeneca.insights_service.domain.ports.in.ManageInsightUseCase;
import com.astrazeneca.insights_service.infrastructure.web.dto.CreateInsightRequest;
import com.astrazeneca.insights_service.infrastructure.web.dto.InsightResponse;
import com.astrazeneca.insights_service.infrastructure.web.mapper.WebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/insights")
@Tag(name = "Insight Management", description = "APIs for managing insights")
public class InsightController {

    private final ManageInsightUseCase manageInsightUseCase;
    private final WebMapper webMapper;

    public InsightController(ManageInsightUseCase manageInsightUseCase, WebMapper webMapper) {
        this.manageInsightUseCase = manageInsightUseCase;
        this.webMapper = webMapper;
    }

    @Operation(summary = "Create a new insight", description = "Creates a new insight with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Insight created successfully",
                    content = @Content(schema = @Schema(implementation = InsightResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<InsightResponse> createInsight(@Valid @RequestBody CreateInsightRequest request) {
        Insight insight = manageInsightUseCase.createInsight(webMapper.toCreateCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(webMapper.toResponse(insight));
    }

    @Operation(summary = "Get all insights", description = "Retrieves all insights, optionally filtered by therapeutic area")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved insights")
    })
    @GetMapping
    public ResponseEntity<List<InsightResponse>> getAllInsights(
            @Parameter(description = "Filter by therapeutic area")
            @RequestParam(required = false) String therapeuticArea) {
        List<Insight> insights = manageInsightUseCase.getAllInsights(therapeuticArea);
        return ResponseEntity.ok(webMapper.toResponseList(insights));
    }

    @Operation(summary = "Get insight by ID", description = "Retrieves a specific insight by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insight found",
                    content = @Content(schema = @Schema(implementation = InsightResponse.class))),
            @ApiResponse(responseCode = "404", description = "Insight not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<InsightResponse> getInsightById(
            @Parameter(description = "Unique identifier of the insight") @PathVariable UUID id) {
        return manageInsightUseCase.getInsightById(id)
                .map(insight -> ResponseEntity.ok(webMapper.toResponse(insight)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update an insight", description = "Updates an existing insight with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insight updated successfully",
                    content = @Content(schema = @Schema(implementation = InsightResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Insight not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<InsightResponse> updateInsight(
            @Parameter(description = "Unique identifier of the insight") @PathVariable UUID id,
            @Valid @RequestBody CreateInsightRequest request) {
        Insight insight = manageInsightUseCase.updateInsight(id, webMapper.toUpdateCommand(request));
        return ResponseEntity.ok(webMapper.toResponse(insight));
    }

    @Operation(summary = "Delete an insight", description = "Deletes an insight by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Insight deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Insight not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsight(
            @Parameter(description = "Unique identifier of the insight") @PathVariable UUID id) {
        manageInsightUseCase.deleteInsight(id);
        return ResponseEntity.noContent().build();
    }
}
