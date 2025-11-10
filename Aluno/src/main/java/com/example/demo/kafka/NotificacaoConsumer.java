package com.example.demo.kafka;

import com.example.demo.model.NotificacaoEvento;
import com.example.demo.service.NotificacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoConsumer {

    private final NotificacaoService notificacaoService;
    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "para-aluno", groupId = "notificacao-group")
    public void consumirMensagem(String mensagem) {
        try {
            NotificacaoEvento evento = mapper.readValue(mensagem, NotificacaoEvento.class);
            notificacaoService.processarEvento(evento);
        } catch (Exception e) {
            log.error("Erro ao processar mensagem Kafka: {}", mensagem, e);
        }
    }
}
