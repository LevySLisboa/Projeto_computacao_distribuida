package com.example.Professor.service;

import com.example.Professor.model.NotificacaoEvento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificacaoService {

    public void processarEvento(NotificacaoEvento evento) {
        log.info("🔔 Notificação recebida: {}", evento);

        switch (evento.getTipo()) {
            case "CRIACAO" -> enviarEmail(evento, "Novo Professor criado");
            case "ATUALIZACAO" -> enviarEmail(evento, "Professor atualizado");
            case "EXCLUSAO" -> enviarEmail(evento, "Professor excluído");
            case "LISTAGEM" -> enviarEmail(evento, "Listando Professores");
            case "CONSULTA" -> enviarEmail(evento, "Consultando Professor");
            default -> log.warn("Tipo de evento desconhecido: {}", evento.getTipo());
        }
    }

    private void enviarEmail(NotificacaoEvento evento, String assunto) {
        log.info("📧 Enviando email com assunto '{}' para '{}': {}", assunto, evento.getDestino(), evento.getMensagem());
    }
}