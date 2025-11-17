package com.example.demo.service;

import com.example.demo.model.Aluno;
import com.example.demo.repository.AlunoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AlunoService {

    @Autowired
    private AlunoRepository repo;
    private final Random random = new Random();


    public Aluno criarALuno(Aluno aluno) {
        repo.findByCpf(aluno.getCpf()).ifPresent(a -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        });
        repo.findByEmail(aluno.getEmail()).ifPresent(a -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        });
        log.info("Novo aluno criado com matricula " + aluno.getCpf());
        return repo.save(aluno);
    }

    @CircuitBreaker(name = "alunoService", fallbackMethod = "fallbackListarAlunos")
    @Retry(name = "alunoService")
    @TimeLimiter(name = "alunoService")
    public CompletableFuture<List<Aluno>> acharTodos() {
        log.info("Tentando buscar alunos...");
        simularFalhaComProbabilidade();
        return CompletableFuture.supplyAsync(repo::findAll);
    }

    public Aluno acharPorMatricula(Long matricula) {
        return repo.findById(matricula).orElseThrow();
    }

    public void deletarAluno(Long matricula) {
        log.info("Removendo aluno com matrícula: {}", matricula);
        Aluno aluno = acharPorMatricula(matricula);
        repo.delete(aluno);
        log.info("Aluno removido com sucesso: id={}, cpf={}", aluno.getId(), aluno.getCpf());
    }

    public Aluno atualizarAluno(Aluno newAluno) {
        Long id = newAluno.getId();
        if (id == null) {
            log.warn("Tentativa de atualizar aluno sem id");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do aluno é obrigatório para atualização");
        }

        log.info("Atualizando aluno id={}", id);
        Aluno aluno = repo.findById(id).orElseThrow(() -> {
            log.warn("Aluno não encontrado para atualização: id={}", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado com id " + id);
        });

        String novoCpf = newAluno.getCpf();
        if (novoCpf != null && !novoCpf.equals(aluno.getCpf())) {
            repo.findByCpf(novoCpf).ifPresent(existing -> {
                log.warn("Tentativa de atualizar CPF para um já cadastrado: {} (id existente={})", novoCpf, existing.getId());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado por outro aluno");
            });
            log.debug("Atualizando CPF de {} para {}", aluno.getCpf(), novoCpf);
            aluno.setCpf(novoCpf);
        }

        String novoEmail = newAluno.getEmail();
        if (novoEmail != null && !novoEmail.equals(aluno.getEmail())) {
            repo.findByEmail(novoEmail).ifPresent(existing -> {
                log.warn("Tentativa de atualizar email para um já cadastrado: {} (id existente={})", novoEmail, existing.getId());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado por outro aluno");
            });
            log.debug("Atualizando email de {} para {}", aluno.getEmail(), novoEmail);
            aluno.setEmail(novoEmail);
        }

        if (newAluno.getName() != null) aluno.setName(newAluno.getName());
        if (newAluno.getCurso() != null) aluno.setCurso(newAluno.getCurso());
        if (newAluno.getAulas() != null) aluno.setAulas(newAluno.getAulas());

        if (newAluno.getSenha_hash() != null && !newAluno.getSenha_hash().isBlank()) {
            log.warn("Atualização de senha via atualizarAluno não é recomendada. Implemente fluxo dedicado.");
            aluno.setSenha_hash(newAluno.getSenha_hash());
        }

        Aluno atualizado = repo.save(aluno);
        log.info("Aluno atualizado com sucesso: id={}, cpf={}", atualizado.getId(), atualizado.getCpf());
        return atualizado;
    }

    public CompletableFuture<List<Aluno>> fallbackListarAlunos(Exception ex) {
        log.warn("Fallback acionado: {}", ex.getMessage());

        Aluno alunoPadrao = Aluno.builder()
                .id(-1L)
                .cpf("00000000000")
                .name("Aluno Indisponível")
                .senha_hash("")
                .email("indisponivel@exemplo.com")
                .curso("Serviço temporariamente fora do ar")
                .aulas(List.of("Nenhuma aula disponível"))
                .build();

        return CompletableFuture.completedFuture(List.of(alunoPadrao));
    }
    private void simularFalhaComProbabilidade() {
        if (random.nextInt(10) < 7) { // 70% de chance de falhar
            throw new RuntimeException("Falha simulada no serviço de produtos");
        }
    }
}
