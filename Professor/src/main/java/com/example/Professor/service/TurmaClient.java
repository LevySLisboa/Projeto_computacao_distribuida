package com.example.Professor.service;

import com.example.Professor.model.dto.TurmaDTO;
import com.example.Professor.model.dto.TurmaLightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "turma-service", url = "http://app-gestao-turmas:8080")
public interface TurmaClient {

    @GetMapping("/turmas/{id}")
    TurmaDTO buscarTurma(@PathVariable("id") Long id);

    @GetMapping("/turmas/light/{id}")
    TurmaLightDTO buscarTurmaLight(@PathVariable Long id);

}

