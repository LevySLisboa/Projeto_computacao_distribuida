package com.example.TecnicoAdministrativo.controller.clients;
import com.example.TecnicoAdministrativo.model.DTO.AlunoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "aluno-service", url = "http://localhost:8080/aluno")//mudar localhost por app_alunos quando subir pro docker
public interface AlunoClient {

    @GetMapping("/todos")
    List<AlunoDTO> listarAlunos();

    @GetMapping("/{matricula}")
    AlunoDTO acharPorMatricula(@PathVariable("matricula") Long matricula);

    @DeleteMapping("/{id}")
    void deletarAluno(@PathVariable Long id);
}


