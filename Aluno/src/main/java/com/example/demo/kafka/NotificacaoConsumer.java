package com.example.demo.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoConsumer {
    @KafkaListener(topics = "notificacoes", groupId = "aluno-group")
    public void consumirMensagem(String mensagem) {
        System.out.println("Mensagem recebida pelo Aluno: " + mensagem);
    }
}
