package com.gameplatform.trustscoreservice.dto;

import com.gameplatform.trustscoreservice.domain.enums.Recommendation;
import com.gameplatform.trustscoreservice.domain.enums.RiskLevel;

import java.math.BigDecimal;

public record TrustScoreCalculationResult(
        BigDecimal score,
        RiskLevel riskLevel,
        Recommendation recommendation
) {
}
