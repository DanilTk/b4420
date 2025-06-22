package com.example.logging;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KafkaCorrelationProducerInterceptor implements ProducerInterceptor<Object, Object> {
    public static final String CORRELATION_ID_HEADER = "correlation-id";

    @Override
    public ProducerRecord<Object, Object> onSend(ProducerRecord<Object, Object> record) {
        String correlationId = MDC.get(CORRELATION_ID_HEADER);
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }
        record.headers().add(CORRELATION_ID_HEADER, correlationId.getBytes(UTF_8));
        return record;
    }

    @Override public void onAcknowledgement(RecordMetadata metadata, Exception exception) {}
    @Override public void close() {}
    @Override public void configure(Map<String, ?> configs) {}
}
