package com.example.Professor.model.dto;

import lombok.Data;

@Data
public class TurmaDTO {
    private Long id;
    private String name;
    private String horario;

    public TurmaDTO(Long id, String name, String horario) {
        this.id = id;
        this.name = name;
        this.horario = horario;
    }
}
