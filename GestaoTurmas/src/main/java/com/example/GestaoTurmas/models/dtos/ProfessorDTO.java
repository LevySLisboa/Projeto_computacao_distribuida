package com.example.GestaoTurmas.models.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorDTO {

    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String curso;
    private List<Long> turmasIds;

    public ProfessorDTO(Long id, String name, String curso) {
        this.id = id;
        this.name = name;
        this.curso = curso;
    }
}

