package com.example.TecnicoAdministrativo.controller.clients;
import com.example.TecnicoAdministrativo.model.DTO.AlunoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "aluno-service", url = "http://app-alunos:8080")//mudar localhost por app_alunos quando subir pro docker
public interface AlunoClient {

    @GetMapping("/aluno/todos")
    List<AlunoDTO> listarAlunos();

    @GetMapping("/aluno/{matricula}")
    AlunoDTO acharPorMatricula(@PathVariable("matricula") Long matricula);

    @DeleteMapping("/aluno/{id}")
    void deletarAluno(@PathVariable Long id);
}


