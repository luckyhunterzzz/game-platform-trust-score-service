package com.gameplatform.trustscoreservice.domain.event;

import com.gameplatform.trustscoreservice.domain.enums.Recommendation;
import com.gameplatform.trustscoreservice.domain.enums.RiskLevel;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TrustScoreCalculatedEvent(
        UUID eventId,
        OffsetDateTime occurredAt,

        UUID applicationId,
        UUID offerId,
        UUID userId,

        BigDecimal score,
        RiskLevel riskLevel,
        Recommendation recommendation,

        UUID sourceEventId
) {}
