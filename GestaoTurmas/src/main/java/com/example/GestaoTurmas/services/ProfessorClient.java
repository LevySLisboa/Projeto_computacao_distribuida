package com.example.GestaoTurmas.services;

import com.example.GestaoTurmas.config.FeignConfig;
import com.example.GestaoTurmas.models.dtos.ProfessorBasicDTO;
import com.example.GestaoTurmas.models.dtos.ProfessorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "professor-service", url = "http://app-professor:8080")
//@FeignClient(name = "professor-service", url = "http://localhost:8081",configuration = FeignConfig.class)

public interface ProfessorClient {

    @GetMapping("/professor/{id}")
    ProfessorBasicDTO acharPorId(@PathVariable("id") Long id);

    // 📚 Listar todos
    @GetMapping("/professor/todos")
    List<ProfessorDTO> listarTodos();

    @PostMapping("/professor/{profId}/turmas/{turmaId}")
    ProfessorDTO adicionarTurma(@PathVariable Long profId , @PathVariable Long turmaId);


}

