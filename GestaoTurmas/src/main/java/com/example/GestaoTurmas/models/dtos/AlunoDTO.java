package com.example.GestaoTurmas.models.dtos;

import lombok.Data;

@Data
public class AlunoDTO {
    private Long id;
    private String cpf;
    private String name;
    private String email;
    private String curso;
}
