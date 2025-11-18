package com.example.TecnicoAdministrativo.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ProfessorDTO {
    private Long id;
    private String cpf;
    private String name;
    private String email;
    private String curso;
    private List<String> turmas;
    private List<String> horarios;
}
