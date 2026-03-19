package com.gameplatform.trustscoreservice.service;

import com.gameplatform.trustscoreservice.domain.enums.Recommendation;
import com.gameplatform.trustscoreservice.domain.enums.RiskLevel;
import com.gameplatform.trustscoreservice.dto.TrustScoreCalculationResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class FakeTrustScoreCalculatorService {
    public TrustScoreCalculationResult calculate() {
        int scenario = ThreadLocalRandom.current().nextInt(3);

        return switch (scenario) {
            case 0 -> new TrustScoreCalculationResult(
                    randomBigDecimal(0.80, 0.95),
                    RiskLevel.LOW,
                    Recommendation.APPROVE
            );
            case 1 -> new TrustScoreCalculationResult(
                    randomBigDecimal(0.50, 0.79),
                    RiskLevel.MEDIUM,
                    Recommendation.MANUAL_REVIEW
            );
            default -> new TrustScoreCalculationResult(
                    randomBigDecimal(0.10, 0.49),
                    RiskLevel.HIGH,
                    Recommendation.REJECT
            );
        };
    }

    private BigDecimal randomBigDecimal(double min, double max) {
        double value = ThreadLocalRandom.current().nextDouble(min, max);
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
