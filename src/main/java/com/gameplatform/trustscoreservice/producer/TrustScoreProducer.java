package com.gameplatform.trustscoreservice.producer;

import com.gameplatform.trustscoreservice.domain.event.TrustScoreCalculatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrustScoreProducer {

    @Value("${app.kafka.topics.trust-score-events}")
    private String trustScoreEventsTopic;
    private final KafkaTemplate<String, TrustScoreCalculatedEvent> kafkaTemplate;

    public void send(TrustScoreCalculatedEvent event) {
        log.info(
                "Publishing TrustScoreCalculatedEvent: eventId={}, sourceEventId={}, userId={}, applicationId={}, topic={}",
                event.eventId(),
                event.sourceEventId(),
                event.userId(),
                event.applicationId(),
                trustScoreEventsTopic
        );
        kafkaTemplate.send(
                trustScoreEventsTopic,
                event.userId().toString(),
                event
        );
    }
}