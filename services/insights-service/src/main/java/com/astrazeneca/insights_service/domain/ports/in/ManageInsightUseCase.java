package com.astrazeneca.insights_service.domain.ports.in;

import com.astrazeneca.insights_service.domain.model.ImpactLevel;
import com.astrazeneca.insights_service.domain.model.Insight;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ManageInsightUseCase {

    Insight createInsight(CreateInsightCommand command);

    Optional<Insight> getInsightById(UUID id);

    Insight updateInsight(UUID id, UpdateInsightCommand command);

    void deleteInsight(UUID id);

    List<Insight> getAllInsights(String therapeuticArea);

    record CreateInsightCommand(
            String description,
            String therapeuticArea,
            UUID competitorId,
            ImpactLevel impactLevel
    ) {}

    record UpdateInsightCommand(
            String description,
            String therapeuticArea,
            UUID competitorId,
            ImpactLevel impactLevel
    ) {}
}

