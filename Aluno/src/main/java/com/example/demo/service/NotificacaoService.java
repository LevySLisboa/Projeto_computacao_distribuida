package com.example.demo.service;

import com.example.demo.model.NotificacaoEvento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificacaoService {

    public void processarEvento(NotificacaoEvento evento) {
        log.info("🔔 Notificação recebida: {}", evento);

        switch (evento.getTipo()) {
            case "CRIACAO" -> enviarEmail(evento, "Novo aluno criado");
            case "ATUALIZACAO" -> enviarEmail(evento, "Aluno atualizado");
            case "EXCLUSAO" -> enviarEmail(evento, "Aluno excluído");
            case "LISTAGEM" -> enviarEmail(evento, "Listando Alunos");
            case "CONSULTA" -> enviarEmail(evento, "Consultando Aluno");
            default -> log.warn("Tipo de evento desconhecido: {}", evento.getTipo());
        }
    }

    private void enviarEmail(NotificacaoEvento evento, String assunto) {
        log.info("📧 Enviando email com assunto '{}' para '{}': {}", assunto, evento.getDestino(), evento.getMensagem());
    }
}
