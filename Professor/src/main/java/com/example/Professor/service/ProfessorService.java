package com.example.Professor.service;

import com.example.Professor.model.Professor;
import com.example.Professor.model.dto.ProfessorDetalheDTO;
import com.example.Professor.model.dto.TurmaDTO;
import com.example.Professor.model.dto.TurmaLightDTO;
import com.example.Professor.repository.ProfessorRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProfessorService {

    @Autowired
    private ProfessorRepository repo;

    @Autowired
    private TurmaClient turmaClient;

    @Autowired
    private AlunoClient alunoClient;

    public Professor salvar(Professor professor) {
        repo.findByCpf(professor.getCpf()).ifPresent(a -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        });
        repo.findByEmail(professor.getEmail()).ifPresent(a -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        });
        log.info("Novo professor criado com cpf " + professor.getCpf());
        return repo.save(professor);
    }

    @CircuitBreaker(name = "professorService", fallbackMethod = "fallbackListarProfessores")
    @Retry(name = "professorService")
    @TimeLimiter(name = "professorService")
    public CompletableFuture<List<Professor>> listar() {
        log.info("Tentando buscar professores...");
        return CompletableFuture.supplyAsync(repo::findAll);
    }

    public Professor buscar(Long id){
        return repo.findById(id).orElse(null);
    }

    public ProfessorDetalheDTO buscarComTurmas(Long id) {
        Professor prof = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        // Apenas retorna os IDs das turmas, sem chamar turmas
        List<TurmaDTO> turmas = prof.getTurmasIds().stream()
                .map(turmaId -> {
                    TurmaLightDTO t = turmaClient.buscarTurmaLight(turmaId);
                    return new TurmaDTO(t.id(), t.name(), t.horario());
                })
                .toList();



        return new ProfessorDetalheDTO(
                prof.getId(),
                prof.getName(),
                prof.getEmail(),
                prof.getCpf(),
                prof.getCurso(),
                turmas
        );
    }


    public void deletar(Long id) {
        log.info("Removendo aluno com matrícula: {}", id);
        Professor professor = repo.findById(id).orElseThrow();
        repo.delete(professor);
        log.info("Aluno removido com sucesso: id={}, cpf={}", professor.getId(), professor.getCpf());
        repo.deleteById(id);
    }

    public Professor adicionarTurma(Long professorId, Long turmaId) {
        Professor prof = repo.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        // lista de turmas (List<Long>)
        List<Long> turmas = prof.getTurmasIds();

        if (!turmas.contains(turmaId)) {
            turmas.add(turmaId);
        }

        prof.setTurmasIds(turmas);

        return repo.save(prof);
    }



    public Professor removerTurma(Long professorId, Long turmaId) {
        Professor p = buscar(professorId);
        if (p == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado");
        boolean removed = p.getTurmasIds().removeIf(id -> id.equals(turmaId));
        if (!removed)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não estava associada ao professor");
        return repo.save(p);
    }


    public Professor atualizarProfessor(Professor newProfessor) {
        Long id = newProfessor.getId();
        if (id == null) {
            log.warn("Tentativa de atualizar professor sem id");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do professor é obrigatório para atualização");
        }

        log.info("Atualizando professor id={}", id);
        Professor professor = repo.findById(id).orElseThrow(() -> {
            log.warn("Professor não encontrado para atualização: id={}", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado com id " + id);
        });

        String novoCpf = newProfessor.getCpf();
        if (novoCpf != null && !novoCpf.equals(professor.getCpf())) {
            repo.findByCpf(novoCpf).ifPresent(existing -> {
                log.warn("Tentativa de atualizar CPF para um já cadastrado: {} (id existente={})", novoCpf, existing.getId());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado por outro professor");
            });
            log.debug("Atualizando CPF de {} para {}", professor.getCpf(), novoCpf);
            professor.setCpf(novoCpf);
        }

        String novoEmail = newProfessor.getEmail();
        if (novoEmail != null && !novoEmail.equals(professor.getEmail())) {
            repo.findByEmail(novoEmail).ifPresent(existing -> {
                log.warn("Tentativa de atualizar email para um já cadastrado: {} (id existente={})", novoEmail, existing.getId());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado por outro professor");
            });
            log.debug("Atualizando email de {} para {}", professor.getEmail(), novoEmail);
            professor.setEmail(novoEmail);
        }

        if (newProfessor.getName() != null) professor.setName(newProfessor.getName());
        if (newProfessor.getCurso() != null) professor.setCurso(newProfessor.getCurso());
        if (newProfessor.getTurmasIds() != null) professor.setTurmasIds(newProfessor.getTurmasIds());

        if (newProfessor.getSenha_hash() != null && !newProfessor.getSenha_hash().isBlank()) {
            log.warn("Atualização de senha via atualizarProfessor não é recomendada. Implemente fluxo dedicado.");
            professor.setSenha_hash(newProfessor.getSenha_hash());
        }

        Professor atualizado = repo.save(professor);
        log.info("Professor atualizado com sucesso: id={}, cpf={}", atualizado.getId(), atualizado.getCpf());
        return atualizado;
    }



    public CompletableFuture<List<Professor>> fallbackListarProfessores(Exception ex) {
        log.warn("Fallback acionado: {}", ex.getMessage());

        Professor professorPadrao = Professor.builder()
                .id(-1L)
                .cpf("00000000000")
                .name("Professor Indisponível")
                .senha_hash("")
                .email("indisponivel@exemplo.com")
                .curso("Serviço temporariamente fora do ar")
                .turmasIds(List.of()) // agora lista vazia de Turma
                .build();

        return CompletableFuture.completedFuture(List.of(professorPadrao));
    }

}

