package com.astrazeneca.competitor_service.infrastructure.web.controller;

import com.astrazeneca.competitor_service.domain.exception.CompetitorNotFoundException;
import com.astrazeneca.competitor_service.domain.exception.InvalidCompetitorDataException;
import com.astrazeneca.competitor_service.domain.model.ClinicalTrial;
import com.astrazeneca.competitor_service.domain.model.Competitor;
import com.astrazeneca.competitor_service.domain.ports.in.ManageCompetitorUseCase;
import com.astrazeneca.competitor_service.infrastructure.web.dto.AddTrialRequest;
import com.astrazeneca.competitor_service.infrastructure.web.dto.CompetitorResponse;
import com.astrazeneca.competitor_service.infrastructure.web.dto.CreateCompetitorRequest;
import com.astrazeneca.competitor_service.infrastructure.web.mapper.WebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompetitorController Tests")
class CompetitorControllerTest {

    @Mock
    private ManageCompetitorUseCase manageCompetitorUseCase;

    @Mock
    private WebMapper webMapper;

    @InjectMocks
    private CompetitorController competitorController;

    private CreateCompetitorRequest createRequest;
    private Competitor competitor;
    private CompetitorResponse competitorResponse;
    private AddTrialRequest addTrialRequest;
    private ClinicalTrial clinicalTrial;

    @BeforeEach
    void setUp() {
        // Setup CreateCompetitorRequest
        createRequest = new CreateCompetitorRequest();
        createRequest.setName("Pfizer");
        createRequest.setHeadquarters("New York, USA");
        createRequest.setTherapeuticAreas(List.of("Oncology", "Vaccines"));

        // Setup Competitor domain model
        competitor = new Competitor();
        competitor.setId(1L);
        competitor.setName("Pfizer");
        competitor.setHeadquarters("New York, USA");
        competitor.setTherapeuticAreas(List.of("Oncology", "Vaccines"));

        // Setup CompetitorResponse
        competitorResponse = new CompetitorResponse();
        competitorResponse.setId(1L);
        competitorResponse.setName("Pfizer");
        competitorResponse.setHeadquarters("New York, USA");
        competitorResponse.setTherapeuticAreas(List.of("Oncology", "Vaccines"));

        // Setup AddTrialRequest
        addTrialRequest = new AddTrialRequest();
        addTrialRequest.setTrialId("NCT12345");
        addTrialRequest.setName("Drug A Trial");
        addTrialRequest.setPhase("Phase III");
        addTrialRequest.setStatus("Active");
        addTrialRequest.setIndication("Lung Cancer");

        // Setup ClinicalTrial domain model
        clinicalTrial = new ClinicalTrial();
        clinicalTrial.setTrialId("NCT12345");
        clinicalTrial.setName("Drug A Trial");
        clinicalTrial.setPhase("Phase III");
        clinicalTrial.setStatus("Active");
        clinicalTrial.setIndication("Lung Cancer");
    }

    @Test
    @DisplayName("Should create competitor successfully")
    void shouldCreateCompetitorSuccessfully() {
        // Given
        when(webMapper.toDomain(createRequest)).thenReturn(competitor);
        when(manageCompetitorUseCase.createCompetitor(competitor)).thenReturn(competitor);
        when(webMapper.toResponse(competitor)).thenReturn(competitorResponse);

        // When
        ResponseEntity<CompetitorResponse> response = competitorController.createCompetitor(createRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getName()).isEqualTo("Pfizer");

        verify(webMapper).toDomain(createRequest);
        verify(manageCompetitorUseCase).createCompetitor(competitor);
        verify(webMapper).toResponse(competitor);
    }

    @Test
    @DisplayName("Should throw exception when creating competitor with invalid data")
    void shouldThrowExceptionWhenCreatingCompetitorWithInvalidData() {
        // Given
        when(webMapper.toDomain(createRequest)).thenReturn(competitor);
        when(manageCompetitorUseCase.createCompetitor(competitor))
                .thenThrow(new InvalidCompetitorDataException("Invalid competitor data"));

        // When & Then
        assertThatThrownBy(() -> competitorController.createCompetitor(createRequest))
                .isInstanceOf(InvalidCompetitorDataException.class)
                .hasMessageContaining("Invalid competitor data");

        verify(webMapper).toDomain(createRequest);
        verify(manageCompetitorUseCase).createCompetitor(competitor);
        verify(webMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should get all competitors successfully")
    void shouldGetAllCompetitorsSuccessfully() {
        // Given
        List<Competitor> competitors = List.of(competitor);
        List<CompetitorResponse> responses = List.of(competitorResponse);

        when(manageCompetitorUseCase.getAllCompetitors()).thenReturn(competitors);
        when(webMapper.toResponseList(competitors)).thenReturn(responses);

        // When
        ResponseEntity<List<CompetitorResponse>> response = competitorController.getAllCompetitors();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Pfizer");

        verify(manageCompetitorUseCase).getAllCompetitors();
        verify(webMapper).toResponseList(competitors);
    }

    @Test
    @DisplayName("Should return empty list when no competitors exist")
    void shouldReturnEmptyListWhenNoCompetitorsExist() {
        // Given
        when(manageCompetitorUseCase.getAllCompetitors()).thenReturn(List.of());
        when(webMapper.toResponseList(List.of())).thenReturn(List.of());

        // When
        ResponseEntity<List<CompetitorResponse>> response = competitorController.getAllCompetitors();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();

        verify(manageCompetitorUseCase).getAllCompetitors();
        verify(webMapper).toResponseList(List.of());
    }

    @Test
    @DisplayName("Should get competitor by ID successfully")
    void shouldGetCompetitorByIdSuccessfully() {
        // Given
        Long competitorId = 1L;
        when(manageCompetitorUseCase.getCompetitorById(competitorId)).thenReturn(competitor);
        when(webMapper.toResponse(competitor)).thenReturn(competitorResponse);

        // When
        ResponseEntity<CompetitorResponse> response = competitorController.getCompetitorById(competitorId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getName()).isEqualTo("Pfizer");

        verify(manageCompetitorUseCase).getCompetitorById(competitorId);
        verify(webMapper).toResponse(competitor);
    }

    @Test
    @DisplayName("Should throw exception when competitor not found by ID")
    void shouldThrowExceptionWhenCompetitorNotFoundById() {
        // Given
        Long competitorId = 999L;
        when(manageCompetitorUseCase.getCompetitorById(competitorId))
                .thenThrow(new CompetitorNotFoundException(competitorId));

        // When & Then
        assertThatThrownBy(() -> competitorController.getCompetitorById(competitorId))
                .isInstanceOf(CompetitorNotFoundException.class)
                .hasMessageContaining("999");

        verify(manageCompetitorUseCase).getCompetitorById(competitorId);
        verify(webMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should add clinical trial to competitor successfully")
    void shouldAddClinicalTrialToCompetitorSuccessfully() {
        // Given
        Long competitorId = 1L;
        competitor.setClinicalTrials(List.of(clinicalTrial));
        competitorResponse.setClinicalTrials(List.of());

        when(webMapper.toDomain(addTrialRequest)).thenReturn(clinicalTrial);
        when(manageCompetitorUseCase.addClinicalTrialToCompetitor(competitorId, clinicalTrial))
                .thenReturn(competitor);
        when(webMapper.toResponse(competitor)).thenReturn(competitorResponse);

        // When
        ResponseEntity<CompetitorResponse> response = competitorController.addClinicalTrial(competitorId, addTrialRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);

        verify(webMapper).toDomain(addTrialRequest);
        verify(manageCompetitorUseCase).addClinicalTrialToCompetitor(competitorId, clinicalTrial);
        verify(webMapper).toResponse(competitor);
    }

    @Test
    @DisplayName("Should throw exception when adding trial to non-existent competitor")
    void shouldThrowExceptionWhenAddingTrialToNonExistentCompetitor() {
        // Given
        Long competitorId = 999L;
        when(webMapper.toDomain(addTrialRequest)).thenReturn(clinicalTrial);
        when(manageCompetitorUseCase.addClinicalTrialToCompetitor(competitorId, clinicalTrial))
                .thenThrow(new CompetitorNotFoundException(competitorId));

        // When & Then
        assertThatThrownBy(() -> competitorController.addClinicalTrial(competitorId, addTrialRequest))
                .isInstanceOf(CompetitorNotFoundException.class)
                .hasMessageContaining("999");

        verify(webMapper).toDomain(addTrialRequest);
        verify(manageCompetitorUseCase).addClinicalTrialToCompetitor(competitorId, clinicalTrial);
        verify(webMapper, never()).toResponse(any());
    }
}