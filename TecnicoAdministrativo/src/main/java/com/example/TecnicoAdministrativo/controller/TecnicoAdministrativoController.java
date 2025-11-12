package com.example.TecnicoAdministrativo.controller;

import com.example.demo.service.AlunoService;
import com.example.Professor.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173") // Ajuste conforme necessário
public class TecnicoAdminController {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private ProfessorService professorService;

    // ============ GERENCIAR ALUNOS ============

    @GetMapping("/alunos")
    public ResponseEntity<?> listarAlunos() {
        try {
            List<?> alunos = alunoService.acharTodos();
            return ResponseEntity.ok(alunos);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Erro ao listar alunos: " + e.getMessage()));
        }
    }

    @DeleteMapping("/alunos/{id}")
    public ResponseEntity<?> excluirAluno(@PathVariable Long id) {
        try {
            // Verifica se o aluno existe
            var aluno = alunoService.acharPorMatricula(id);
            if (aluno == null) {
                return ResponseEntity.status(404)
                        .body(Map.of("error", "Aluno não encontrado"));
            }

            alunoService.deletarAluno(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Aluno excluído com sucesso");
            response.put("id", id);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Erro ao excluir aluno: " + e.getMessage()));
        }
    }

    // ============ GERENCIAR PROFESSORES ============

    @GetMapping("/professores")
    public ResponseEntity<?> listarProfessores() {
        try {
            List<?> professores = (List<?>) professorService.listar();
            return ResponseEntity.ok(professores);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Erro ao listar professores: " + e.getMessage()));
        }
    }

    @DeleteMapping("/professores/{id}")
    public ResponseEntity<?> excluirProfessor(@PathVariable Long id) {
        try {
            // Verifica se o professor existe
            var professor = professorService.buscar(id);
            if (professor == null) {
                return ResponseEntity.status(404)
                        .body(Map.of("error", "Professor não encontrado"));
            }

            professorService.deletar(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Professor excluído com sucesso");
            response.put("id", id);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Erro ao excluir professor: " + e.getMessage()));
        }
    }

}