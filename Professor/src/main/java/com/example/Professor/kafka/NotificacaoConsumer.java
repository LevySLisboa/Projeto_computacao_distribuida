package com.example.Professor.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoConsumer {
    @KafkaListener(topics = "para-professor")
    public void consumirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}