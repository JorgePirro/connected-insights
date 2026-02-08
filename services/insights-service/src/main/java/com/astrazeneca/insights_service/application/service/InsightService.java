package com.astrazeneca.insights_service.application.service;

import com.astrazeneca.insights_service.domain.model.Insight;
import com.astrazeneca.insights_service.domain.ports.in.ManageInsightUseCase;
import com.astrazeneca.insights_service.domain.ports.out.InsightRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class InsightService implements ManageInsightUseCase {

    private final InsightRepositoryPort insightRepository;

    public InsightService(InsightRepositoryPort insightRepository) {
        this.insightRepository = insightRepository;
    }

    @Override
    public Insight createInsight(CreateInsightCommand command) {
        Insight insight = Insight.create(
                command.description(),
                command.therapeuticArea(),
                command.competitorId(),
                command.impactLevel()
        );
        return insightRepository.save(insight);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Insight> getInsightById(UUID id) {
        return insightRepository.findById(id);
    }

    @Override
    public Insight updateInsight(UUID id, UpdateInsightCommand command) {
        Insight insight = insightRepository.findById(id)
                .orElseThrow(() -> new InsightNotFoundException(id));

        insight.update(
                command.description(),
                command.therapeuticArea(),
                command.competitorId(),
                command.impactLevel()
        );

        return insightRepository.save(insight);
    }

    @Override
    public void deleteInsight(UUID id) {
        if (!insightRepository.existsById(id)) {
            throw new InsightNotFoundException(id);
        }
        insightRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Insight> getAllInsights(String therapeuticArea) {
        if (therapeuticArea != null && !therapeuticArea.isBlank()) {
            return insightRepository.findByTherapeuticArea(therapeuticArea);
        }
        return insightRepository.findAll();
    }

    public static class InsightNotFoundException extends RuntimeException {
        public InsightNotFoundException(UUID id) {
            super("Insight not found with id: " + id);
        }
    }
}

