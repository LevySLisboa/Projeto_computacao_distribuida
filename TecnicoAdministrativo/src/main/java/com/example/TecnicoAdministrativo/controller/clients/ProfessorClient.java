package com.example.TecnicoAdministrativo.controller.clients;

import com.example.TecnicoAdministrativo.model.DTO.ProfessorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "professor-service", url = "http://app-professor:8080")
public interface ProfessorClient {
    @GetMapping("/professor/todos")
    List<ProfessorDTO> listarProfessores();

    @GetMapping("/professor/{id}")
    ProfessorDTO acharPorId(@PathVariable("id") Long id);

    @DeleteMapping("/professor/{id}")
    void deletarProfessor(@PathVariable Long id);
}
