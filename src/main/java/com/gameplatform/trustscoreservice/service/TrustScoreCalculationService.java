package com.gameplatform.trustscoreservice.service;

import com.gameplatform.trustscoreservice.domain.event.ParticipationApplicationSubmittedEvent;
import com.gameplatform.trustscoreservice.domain.event.TrustScoreCalculatedEvent;
import com.gameplatform.trustscoreservice.domain.redis.TrustScoreSnapshot;
import com.gameplatform.trustscoreservice.dto.TrustScoreCalculationResult;
import com.gameplatform.trustscoreservice.producer.TrustScoreProducer;
import com.gameplatform.trustscoreservice.repository.TrustScoreRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class TrustScoreCalculationService {

    private final FakeTrustScoreCalculatorService fakeTrustScoreCalculator;
    private final TrustScoreRedisRepository trustScoreRedisRepository;
    private final TrustScoreProducer trustScoreProducer;

    public void calculateForSubmittedApplication(ParticipationApplicationSubmittedEvent event) {
        try {
            TrustScoreCalculationResult result = fakeTrustScoreCalculator.calculate();

            OffsetDateTime calculatedAt = OffsetDateTime.now();
            UUID calculatedEventId = UUID.randomUUID();

            TrustScoreSnapshot snapshot = TrustScoreSnapshot.builder()
                    .userId(event.applicantUserId())
                    .score(result.score())
                    .riskLevel(result.riskLevel())
                    .recommendation(result.recommendation())
                    .calculatedAt(calculatedAt)
                    .sourceEventId(event.eventId())
                    .applicationId(event.applicationId())
                    .offerId(event.offerId())
                    .build();

            trustScoreRedisRepository.save(snapshot);

            TrustScoreCalculatedEvent calculatedEvent = TrustScoreCalculatedEvent.builder()
                    .eventId(calculatedEventId)
                    .occurredAt(calculatedAt)
                    .applicationId(event.applicationId())
                    .offerId(event.offerId())
                    .userId(event.applicantUserId())
                    .score(result.score())
                    .riskLevel(result.riskLevel())
                    .recommendation(result.recommendation())
                    .sourceEventId(event.eventId())
                    .build();



            trustScoreProducer.send(calculatedEvent);

            log.info(
                    "Trust score calculated and published: userId={}, applicationId={}, score={}, riskLevel={}, recommendation={}",
                    event.applicantUserId(),
                    event.applicationId(),
                    result.score(),
                    result.riskLevel(),
                    result.recommendation()
            );
        } catch (Exception ex) {
            log.error(
                    "Failed to process ParticipationApplicationSubmittedEvent: eventId={}, applicationId={}, applicantUserId={}",
                    event.eventId(),
                    event.applicationId(),
                    event.applicantUserId(),
                    ex
            );
            throw ex;
        }
    }
}
