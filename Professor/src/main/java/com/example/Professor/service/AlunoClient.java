package com.example.Professor.service;

import com.example.Professor.model.dto.AlunoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "aluno-service", url = "http://app-alunos:8080")//mudar localhost por app_alunos quando subir pro docker
//@FeignClient(name = "aluno-service", url = "http://localhost:8080")//mudar localhost por app_alunos quando subir pro docker
public interface AlunoClient {

    @GetMapping("/aluno/{matricula}")
    AlunoDTO buscarPorId(@PathVariable Long matricula);

    @GetMapping("/aluno")
    List<AlunoDTO> listarTodos();

}
