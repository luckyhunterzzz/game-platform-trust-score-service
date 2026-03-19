package com.gameplatform.trustscoreservice.domain.redis;

import com.gameplatform.trustscoreservice.domain.enums.Recommendation;
import com.gameplatform.trustscoreservice.domain.enums.RiskLevel;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrustScoreSnapshot {
    private UUID userId;
    private BigDecimal score;
    private RiskLevel riskLevel;
    private Recommendation recommendation;

    private OffsetDateTime calculatedAt;

    private UUID sourceEventId;
    private UUID applicationId;
    private UUID offerId;
}
