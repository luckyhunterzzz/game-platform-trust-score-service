package com.gameplatform.trustscoreservice.domain.event;

import java.time.OffsetDateTime;
import java.util.UUID;


public record ParticipationApplicationSubmittedEvent(
        UUID eventId,
        OffsetDateTime occurredAt,

        UUID applicationId,
        UUID offerId,
        UUID applicantUserId
) {}
