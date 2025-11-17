package com.example.TecnicoAdministrativo.model.DTO;

import lombok.Data;

import java.util.List;
@Data
public class AlunoDTO {
    private Long id;
    private String cpf;
    private String name;
    private String email;
    private String curso;
    private List<String> aulas;
}

