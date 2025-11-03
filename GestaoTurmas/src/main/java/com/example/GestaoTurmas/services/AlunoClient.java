package com.example.GestaoTurmas.services;

import com.example.GestaoTurmas.models.dtos.AlunoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class AlunoClient {
    private final RestTemplate rt = new RestTemplate();
    private final String baseUrl= "http://localhost:8080/aluno/";

    public AlunoDTO buscarPorId(Long id){
        return rt.getForObject(baseUrl + id, AlunoDTO.class);
    }
    public List<AlunoDTO> listarTodos() {
        AlunoDTO[] alunos = rt.getForObject(baseUrl +"todos", AlunoDTO[].class);
        return Arrays.asList(alunos);
    }
}
