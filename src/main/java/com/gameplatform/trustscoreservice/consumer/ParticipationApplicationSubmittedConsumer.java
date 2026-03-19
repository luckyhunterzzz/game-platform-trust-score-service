package com.gameplatform.trustscoreservice.consumer;

import com.gameplatform.trustscoreservice.domain.event.ParticipationApplicationSubmittedEvent;
import com.gameplatform.trustscoreservice.service.TrustScoreCalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipationApplicationSubmittedConsumer {

    private final TrustScoreCalculationService trustScoreCalculationService;

    @KafkaListener(
            topics = "${app.kafka.topics.joint-purchase-events}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "participationApplicationSubmittedKafkaListenerContainerFactory"
    )
    public void handle(ParticipationApplicationSubmittedEvent event) {
        log.info(
                "Received ParticipationApplicationSubmittedEvent: " +
                        "eventId={}, applicationId={}, offerId={}, applicantUserId={}",
                event.eventId(),
                event.applicationId(),
                event.offerId(),
                event.applicantUserId()
        );

        trustScoreCalculationService.calculateForSubmittedApplication(event);
    }
}
