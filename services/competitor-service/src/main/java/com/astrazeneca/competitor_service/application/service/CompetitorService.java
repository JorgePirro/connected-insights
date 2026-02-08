package com.astrazeneca.competitor_service.application.service;

import com.astrazeneca.competitor_service.domain.exception.CompetitorNotFoundException;
import com.astrazeneca.competitor_service.domain.exception.InvalidClinicalTrialException;
import com.astrazeneca.competitor_service.domain.exception.InvalidCompetitorDataException;
import com.astrazeneca.competitor_service.domain.model.ClinicalTrial;
import com.astrazeneca.competitor_service.domain.model.Competitor;
import com.astrazeneca.competitor_service.domain.ports.in.ManageCompetitorUseCase;
import com.astrazeneca.competitor_service.domain.ports.out.CompetitorRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Application Service - Implements Use Case (Input Port)
 * Orchestrates domain logic and persistence
 */
@Service
@Transactional
public class CompetitorService implements ManageCompetitorUseCase {

    private final CompetitorRepositoryPort competitorRepositoryPort;

    public CompetitorService(CompetitorRepositoryPort competitorRepositoryPort) {
        this.competitorRepositoryPort = competitorRepositoryPort;
    }

    @Override
    public Competitor createCompetitor(Competitor competitor) {
        validateCompetitor(competitor);
        return competitorRepositoryPort.save(competitor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Competitor> getAllCompetitors() {
        return competitorRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Competitor getCompetitorById(Long id) {
        if (id == null) {
            throw new InvalidCompetitorDataException("id", "must not be null");
        }
        return competitorRepositoryPort.findById(id)
                .orElseThrow(() -> new CompetitorNotFoundException(id));
    }

    @Override
    public Competitor addClinicalTrialToCompetitor(Long competitorId, ClinicalTrial trial) {
        if (competitorId == null) {
            throw new InvalidCompetitorDataException("competitorId", "must not be null");
        }
        validateClinicalTrial(trial);

        Competitor competitor = competitorRepositoryPort.findById(competitorId)
                .orElseThrow(() -> new CompetitorNotFoundException(competitorId));

        // Domain logic is called here
        competitor.addClinicalTrial(trial);

        return competitorRepositoryPort.save(competitor);
    }

    private void validateCompetitor(Competitor competitor) {
        if (competitor == null) {
            throw new InvalidCompetitorDataException("competitor", "must not be null");
        }
        if (competitor.getName() == null || competitor.getName().isBlank()) {
            throw new InvalidCompetitorDataException("name", "must not be blank");
        }
        if (competitor.getHeadquarters() == null || competitor.getHeadquarters().isBlank()) {
            throw new InvalidCompetitorDataException("headquarters", "must not be blank");
        }
    }

    private void validateClinicalTrial(ClinicalTrial trial) {
        if (trial == null) {
            throw new InvalidClinicalTrialException("clinicalTrial", "must not be null");
        }
        if (trial.getTrialId() == null || trial.getTrialId().isBlank()) {
            throw new InvalidClinicalTrialException("trialId", "must not be blank");
        }
        if (trial.getName() == null || trial.getName().isBlank()) {
            throw new InvalidClinicalTrialException("name", "must not be blank");
        }
        if (trial.getPhase() == null || trial.getPhase().isBlank()) {
            throw new InvalidClinicalTrialException("phase", "must not be blank");
        }
        if (trial.getStatus() == null || trial.getStatus().isBlank()) {
            throw new InvalidClinicalTrialException("status", "must not be blank");
        }
        if (trial.getIndication() == null || trial.getIndication().isBlank()) {
            throw new InvalidClinicalTrialException("indication", "must not be blank");
        }
    }
}
