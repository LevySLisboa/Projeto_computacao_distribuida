package com.example.GestaoTurmas.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurmaResponseDTO {
    private Long id;
    private String name;
    private String horario;
    private ProfessorBasicDTO professor;
    private List<AlunoDTO> alunos;
}

