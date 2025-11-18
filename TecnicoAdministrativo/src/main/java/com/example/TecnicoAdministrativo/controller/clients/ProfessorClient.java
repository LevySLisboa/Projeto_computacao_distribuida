package com.example.TecnicoAdministrativo.controller.clients;

import com.example.TecnicoAdministrativo.model.DTO.ProfessorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "professor-service", url = "http://localhost:8081/professor")//mudar localhost por app_professora quando subir pro docker
public interface ProfessorClient {
    @GetMapping("/todos")
    List<ProfessorDTO> listarProfessores();

    @GetMapping("/{matricula}")
    ProfessorDTO acharPorMatricula(@PathVariable("matricula") Long matricula);

    @DeleteMapping("/{id}")
    void deletarProfessor(@PathVariable Long id);
}
