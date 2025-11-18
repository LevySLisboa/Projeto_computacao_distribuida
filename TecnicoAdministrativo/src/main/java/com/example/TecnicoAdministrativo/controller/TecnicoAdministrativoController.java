package com.example.TecnicoAdministrativo.controller;

import com.example.TecnicoAdministrativo.controller.clients.AlunoClient;
import com.example.TecnicoAdministrativo.controller.clients.ProfessorClient;
import com.example.TecnicoAdministrativo.model.TecnicoAdministrativo;
import com.example.TecnicoAdministrativo.service.TecnicoAdministrativoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class TecnicoAdministrativoController {
    @Autowired
    private TecnicoAdministrativoService service;

    @Autowired
    private AlunoClient alunoClient;
    @Autowired
    private ProfessorClient professorClient;

    @PostMapping("/criarTecnicoAdministrativo")
    public ResponseEntity<TecnicoAdministrativo> criar(@RequestBody TecnicoAdministrativo tecnicoAdministrativo) {
        TecnicoAdministrativo salvo = service.salvar(tecnicoAdministrativo);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<TecnicoAdministrativo>> buscarTodos() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoAdministrativo> buscar(@PathVariable Long id) {
        TecnicoAdministrativo p = service.buscar(id);
        return (p != null) ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/alunos")
    public ResponseEntity<?> listarAlunos() {
        return ResponseEntity.ok(alunoClient.listarAlunos());
    }

    @GetMapping("/alunos/{matricula}")
    public ResponseEntity<?> buscarAlunoPorMatricula(@PathVariable Long matricula) {
        var aluno = alunoClient.acharPorMatricula(matricula);
        return ResponseEntity.ok(aluno);
    }

    @DeleteMapping("/alunos/{id}")
    public ResponseEntity<?> excluirAluno(@PathVariable Long id) {
        alunoClient.deletarAluno(id);
        return ResponseEntity.ok(Map.of("message", "Aluno excluído com sucesso"));
    }
    @GetMapping("/professores")
    public ResponseEntity<?> listarProfessores() {
        return ResponseEntity.ok(professorClient.listarProfessores());
    }

    @GetMapping("/professores/{matricula}")
    public ResponseEntity<?> buscarProfessorPorMatricula(@PathVariable Long matricula) {
        var professor= professorClient.acharPorMatricula(matricula);
        return ResponseEntity.ok(professor);
    }

    @DeleteMapping("/professores/{id}")
    public ResponseEntity<?> excluirProfessor(@PathVariable Long id) {
        professorClient.deletarProfessor(id);
        return ResponseEntity.ok(Map.of("message", "Professor excluído com sucesso"));
    }

}
