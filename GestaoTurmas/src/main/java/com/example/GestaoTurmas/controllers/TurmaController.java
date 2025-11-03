package com.example.GestaoTurmas.controllers;

import com.example.GestaoTurmas.models.Turma;
import com.example.GestaoTurmas.models.dtos.AlunoDTO;
import com.example.GestaoTurmas.repositories.TurmaRepository;
import com.example.GestaoTurmas.services.AlunoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turmas")
public class TurmaController {
    @Autowired
    private TurmaRepository repo;
    @Autowired
    private AlunoClient alunoClient;
    @GetMapping
    public List<Turma> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Turma criar(@RequestBody Turma turma) {
        List<AlunoDTO> alunos = alunoClient.listarTodos();
        if(alunos.contains()){
            return repo.save(turma);
        }
    }

    @GetMapping("/{id}/alunos")
    public List<AlunoDTO> listarAlunosDaTurma(@PathVariable Long id) {
        Turma turma = repo.findById(id).orElseThrow();
        return turma.getAlunosIds().stream()
                .map(alunoClient::buscarPorId)
                .toList();
    }
}