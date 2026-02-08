package com.astrazeneca.insights_service.infrastructure.web.mapper;

import com.astrazeneca.insights_service.domain.model.Insight;
import com.astrazeneca.insights_service.domain.ports.in.ManageInsightUseCase.CreateInsightCommand;
import com.astrazeneca.insights_service.domain.ports.in.ManageInsightUseCase.UpdateInsightCommand;
import com.astrazeneca.insights_service.infrastructure.web.dto.CreateInsightRequest;
import com.astrazeneca.insights_service.infrastructure.web.dto.InsightResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WebMapper {

    CreateInsightCommand toCreateCommand(CreateInsightRequest request);

    UpdateInsightCommand toUpdateCommand(CreateInsightRequest request);

    InsightResponse toResponse(Insight insight);

    List<InsightResponse> toResponseList(List<Insight> insights);
}

