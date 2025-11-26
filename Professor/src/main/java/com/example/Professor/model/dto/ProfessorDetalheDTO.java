package com.example.Professor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProfessorDetalheDTO {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String curso;

    private List<TurmaDTO> turmas;
}

