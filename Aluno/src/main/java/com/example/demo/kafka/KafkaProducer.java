package com.example.demo.kafka;

import com.example.demo.model.NotificacaoEvento;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public void sendEvent(String topic, NotificacaoEvento evento) {
        try {
            String json = mapper.writeValueAsString(evento);
            kafkaTemplate.send(topic, json);
            log.info("Evento enviado ao Kafka: {}", json);
        } catch (Exception e) {
            log.error("Erro ao serializar evento Kafka", e);
        }
    }
}
