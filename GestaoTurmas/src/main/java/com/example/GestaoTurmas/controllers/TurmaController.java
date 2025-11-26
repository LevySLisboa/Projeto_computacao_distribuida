package com.example.GestaoTurmas.controllers;

import com.example.GestaoTurmas.models.Turma;
import com.example.GestaoTurmas.models.dtos.*;
import com.example.GestaoTurmas.repositories.TurmaRepository;
import com.example.GestaoTurmas.services.AlunoClient;
import com.example.GestaoTurmas.services.ProfessorClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/turmas")
public class TurmaController {

    @Autowired
    private TurmaRepository repo;

    @Autowired
    private AlunoClient alunoClient;

    @Autowired
    private ProfessorClient professorClient;

    // -------------------------------------------------------------------------
    // 1. CRIAR TURMA
    // -------------------------------------------------------------------------

    @PostMapping
    public ResponseEntity<TurmaResponseDTO> criarTurma(@RequestBody Turma turma) {

        ProfessorBasicDTO professor = null;
        List<AlunoDTO> alunos = new ArrayList<>();

        // valida professor
        if (turma.getProfessorId() != null) {
            professor = professorClient.acharPorId(turma.getProfessorId());
        }

        // valida alunos
        if (turma.getAlunosIds() != null) {
            for (Long alunoId : turma.getAlunosIds()) {
                alunos.add(alunoClient.buscarPorId(alunoId));
            }
        }

        // salva turma
        Turma salva = repo.save(turma);

        // 🔥 Atualiza o professor com a nova turma criada
        professorClient.adicionarTurma(salva.getProfessorId(), salva.getId());

        // resposta
        TurmaResponseDTO resposta = new TurmaResponseDTO(
                salva.getId(),
                salva.getName(),
                salva.getHorario(),
                professor,
                alunos
        );

        return ResponseEntity.ok(resposta);
    }

    // -------------------------------------------------------------------------
    // 2. ADICIONAR 1 OU VÁRIOS ALUNOS A UMA TURMA
    // -------------------------------------------------------------------------
    @PostMapping("/{turmaId}/adicionar-alunos")
    public ResponseEntity<Turma> adicionarAlunos(
            @PathVariable Long turmaId,
            @RequestBody List<Long> novosAlunosIds
    ) {
        Turma turma = repo.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        // valida alunos
        for (Long id : novosAlunosIds) {
            alunoClient.buscarPorId(id);
        }

        turma.getAlunosIds().addAll(novosAlunosIds);
        repo.save(turma);

        return ResponseEntity.ok(turma);
    }

    // -------------------------------------------------------------------------
    // 3. REMOVER UM ALUNO DA TURMA
    // -------------------------------------------------------------------------
    @DeleteMapping("/{turmaId}/remover-aluno/{alunoId}")
    public ResponseEntity<Turma> removerAluno(
            @PathVariable Long turmaId,
            @PathVariable Long alunoId
    ) {
        Turma turma = repo.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        turma.getAlunosIds().remove(alunoId);
        repo.save(turma);

        return ResponseEntity.ok(turma);
    }

    // -------------------------------------------------------------------------
    // 4. ADICIONAR/ALTERAR PROFESSOR DA TURMA
    // -------------------------------------------------------------------------
    @PostMapping("/{turmaId}/professor/{professorId}")
    public ResponseEntity<Turma> definirProfessor(
            @PathVariable Long turmaId,
            @PathVariable Long professorId
    ) {
        Turma turma = repo.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        // valida professor
        professorClient.acharPorId(professorId);

        turma.setProfessorId(professorId);
        repo.save(turma);

        return ResponseEntity.ok(turma);
    }

    // -------------------------------------------------------------------------
    // 5. REMOVER TURMA
    // -------------------------------------------------------------------------
    @DeleteMapping("/{turmaId}")
    public ResponseEntity<Void> removerTurma(@PathVariable Long turmaId) {
        repo.deleteById(turmaId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/light/{id}")
    public TurmaLightDTO buscarTurmaLight(@PathVariable Long id) {
        Turma turma = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        return new TurmaLightDTO(
                turma.getId(),
                turma.getName(),
                turma.getHorario()
        );
    }

    // -------------------------------------------------------------------------
    // 6. BUSCAR UMA TURMA POR ID
    // -------------------------------------------------------------------------
    @GetMapping("/{turmaId}")
    public ResponseEntity<TurmaResponseDTO> buscarTurma(@PathVariable Long turmaId) {
        Turma turma = repo.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        List<AlunoDTO> alunos = new ArrayList<>();
        ProfessorBasicDTO professor = professorClient.acharPorId(turma.getProfessorId());
        for(Long alunoId:turma.getAlunosIds()){
            AlunoDTO alunoDTO = alunoClient.buscarPorId(alunoId);
            alunos.add(alunoDTO);
        }
        TurmaResponseDTO response = new TurmaResponseDTO(turma.getId(),turma.getName(),turma.getHorario(),
                professor, alunos);
        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------------------------------
    // 7. LISTAR TODAS AS TURMAS
    // -------------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Turma>> listarTodas() {
        return ResponseEntity.ok(repo.findAll());
    }
}
