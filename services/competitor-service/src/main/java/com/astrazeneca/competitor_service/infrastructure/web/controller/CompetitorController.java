package com.astrazeneca.competitor_service.infrastructure.web.controller;

import com.astrazeneca.competitor_service.domain.model.ClinicalTrial;
import com.astrazeneca.competitor_service.domain.model.Competitor;
import com.astrazeneca.competitor_service.domain.ports.in.ManageCompetitorUseCase;
import com.astrazeneca.competitor_service.infrastructure.web.dto.AddTrialRequest;
import com.astrazeneca.competitor_service.infrastructure.web.dto.CompetitorResponse;
import com.astrazeneca.competitor_service.infrastructure.web.dto.CreateCompetitorRequest;
import com.astrazeneca.competitor_service.infrastructure.web.dto.ErrorResponse;
import com.astrazeneca.competitor_service.infrastructure.web.mapper.WebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller - Infrastructure/Web Layer (Web Adapter)
 * Exposes HTTP endpoints and delegates to Use Cases
 */
@RestController
@RequestMapping("/api/competitors")
@Tag(name = "Competitor Management", description = "APIs for managing pharmaceutical competitors and their clinical trials")
public class CompetitorController {

    private final ManageCompetitorUseCase manageCompetitorUseCase;
    private final WebMapper webMapper;

    public CompetitorController(ManageCompetitorUseCase manageCompetitorUseCase,
                               WebMapper webMapper) {
        this.manageCompetitorUseCase = manageCompetitorUseCase;
        this.webMapper = webMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new competitor", description = "Creates a new pharmaceutical competitor with therapeutic areas and headquarters information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Competitor created successfully",
                content = @Content(schema = @Schema(implementation = CompetitorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CompetitorResponse> createCompetitor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Competitor details to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateCompetitorRequest.class)))
            @RequestBody CreateCompetitorRequest request) {
        Competitor competitor = webMapper.toDomain(request);
        Competitor created = manageCompetitorUseCase.createCompetitor(competitor);
        CompetitorResponse response = webMapper.toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all competitors", description = "Retrieves a list of all pharmaceutical competitors")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of competitors",
            content = @Content(schema = @Schema(implementation = CompetitorResponse.class)))
    public ResponseEntity<List<CompetitorResponse>> getAllCompetitors() {
        List<Competitor> competitors = manageCompetitorUseCase.getAllCompetitors();
        List<CompetitorResponse> responses = webMapper.toResponseList(competitors);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get competitor by ID", description = "Retrieves a specific competitor by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Competitor found",
                content = @Content(schema = @Schema(implementation = CompetitorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Competitor not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid ID format",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CompetitorResponse> getCompetitorById(
            @Parameter(description = "ID of the competitor to retrieve", required = true)
            @PathVariable Long id) {
        Competitor competitor = manageCompetitorUseCase.getCompetitorById(id);
        CompetitorResponse response = webMapper.toResponse(competitor);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/trials")
    @Operation(summary = "Add clinical trial to competitor", description = "Adds a new clinical trial to an existing competitor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clinical trial added successfully",
                content = @Content(schema = @Schema(implementation = CompetitorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Competitor not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid trial data",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CompetitorResponse> addClinicalTrial(
            @Parameter(description = "ID of the competitor", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Clinical trial details to add",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddTrialRequest.class)))
            @RequestBody AddTrialRequest request) {
        ClinicalTrial trial = webMapper.toDomain(request);
        Competitor updated = manageCompetitorUseCase.addClinicalTrialToCompetitor(id, trial);
        CompetitorResponse response = webMapper.toResponse(updated);
        return ResponseEntity.ok(response);
    }
}
