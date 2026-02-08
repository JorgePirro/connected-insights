package com.astrazeneca.competitor_service.domain.ports.in;

import com.astrazeneca.competitor_service.domain.model.ClinicalTrial;
import com.astrazeneca.competitor_service.domain.model.Competitor;

import java.util.List;

/**
 * Input Port - Defines use cases for managing competitors
 * Application layer implements this interface
 */
public interface ManageCompetitorUseCase {

    Competitor createCompetitor(Competitor competitor);

    List<Competitor> getAllCompetitors();

    Competitor getCompetitorById(Long id);

    Competitor addClinicalTrialToCompetitor(Long competitorId, ClinicalTrial trial);
}

