package com.gameplatform.trustscoreservice.configuration;

import com.gameplatform.trustscoreservice.domain.event.ParticipationApplicationSubmittedEvent;
import com.gameplatform.trustscoreservice.domain.event.TrustScoreCalculatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, ParticipationApplicationSubmittedEvent>
    participationApplicationSubmittedConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.gameplatform.trustscoreservice.domain.event");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
                "com.gameplatform.trustscoreservice.domain.event.ParticipationApplicationSubmittedEvent");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(ParticipationApplicationSubmittedEvent.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ParticipationApplicationSubmittedEvent>
    participationApplicationSubmittedKafkaListenerContainerFactory(
            ConsumerFactory<String, ParticipationApplicationSubmittedEvent> participationApplicationSubmittedConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, ParticipationApplicationSubmittedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(participationApplicationSubmittedConsumerFactory);
        return factory;
    }

    @Bean
    public ProducerFactory<String, TrustScoreCalculatedEvent> trustScoreCalculatedProducerFactory(
            KafkaProperties kafkaProperties
    ) {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, TrustScoreCalculatedEvent> trustScoreCalculatedKafkaTemplate(
            ProducerFactory<String, TrustScoreCalculatedEvent> trustScoreCalculatedProducerFactory
    ) {
        return new KafkaTemplate<>(trustScoreCalculatedProducerFactory);
    }
}