package com.example.demo.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificacaoEvento {
    private String tipo;    
    private String destino;
    private String mensagem;
    private Long timestamp;
}
