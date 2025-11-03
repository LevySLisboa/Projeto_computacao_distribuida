package com.example.TecnicoAdministrativo.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoConsumer {
    @KafkaListener(topics = "para-tecnico")
    public void consumirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
