package com.example.Professor.model;

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
