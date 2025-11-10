package com.example.demo.controller;

import com.example.demo.kafka.KafkaProducer;
import com.example.demo.model.Aluno;
import com.example.demo.model.NotificacaoEvento;
import com.example.demo.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

    @Autowired
    private AlunoService serv;

    @Autowired
    private KafkaProducer producer;

    // ✅ Criar novo aluno
    @PostMapping("/criarAluno")
    public ResponseEntity<Aluno> criarAluno(@RequestBody Aluno aluno) {
        Aluno salvo = serv.criarALuno(aluno);

        NotificacaoEvento evento = NotificacaoEvento.builder()
                .tipo("CRIACAO")
                .destino("para-tecnico")
                .mensagem("Novo aluno cadastrado: " + salvo)
                .timestamp(System.currentTimeMillis())
                .build();

        producer.sendEvent(evento.getDestino(), evento);

        return ResponseEntity.ok(salvo);
    }

    // ✅ Atualizar aluno existente
    @PostMapping("/atualizarAluno")
    public ResponseEntity<Aluno> atualizarAluno(@RequestBody Aluno aluno) {
        Aluno atualizado = serv.atualizarAluno(aluno);

        NotificacaoEvento evento = NotificacaoEvento.builder()
                .tipo("ATUALIZACAO")
                .destino("para-tecnico")
                .mensagem("Aluno atualizado: " + atualizado)
                .timestamp(System.currentTimeMillis())
                .build();

        producer.sendEvent(evento.getDestino(), evento);

        return ResponseEntity.ok(atualizado);
    }

    // ✅ Deletar aluno por matrícula
    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> deletarAluno(@PathVariable("matricula") Long matricula) {
        Aluno removido = serv.acharPorMatricula(matricula);
        serv.deletarAluno(matricula);

        NotificacaoEvento evento = NotificacaoEvento.builder()
                .tipo("EXCLUSAO")
                .destino("para-tecnico")
                .mensagem("Aluno removido: " + removido)
                .timestamp(System.currentTimeMillis())
                .build();

        producer.sendEvent(evento.getDestino(), evento);

        return ResponseEntity.noContent().build();
    }

    // ✅ Buscar aluno por matrícula
    @GetMapping("/{matricula}")
    public ResponseEntity<Aluno> acharPorMatricula(@PathVariable("matricula") Long matricula) {
        Aluno aluno = serv.acharPorMatricula(matricula);

        NotificacaoEvento evento = NotificacaoEvento.builder()
                .tipo("CONSULTA")
                .destino("para-tecnico")
                .mensagem("Consulta de aluno por matrícula " + matricula + ": " + aluno)
                .timestamp(System.currentTimeMillis())
                .build();

        producer.sendEvent(evento.getDestino(), evento);

        return ResponseEntity.ok(aluno);
    }

    @GetMapping("/todos")
    public CompletableFuture<ResponseEntity<List<Aluno>>> acharTodos() {
        var alunosFuture = serv.acharTodos();

        return alunosFuture.thenApply(alunos -> {
            NotificacaoEvento evento = NotificacaoEvento.builder()
                    .tipo("LISTAGEM")
                    .destino("para-tecnico")
                    .mensagem("Listagem completa de alunos: " + alunos)
                    .timestamp(System.currentTimeMillis())
                    .build();

            producer.sendEvent(evento.getDestino(), evento);

            return ResponseEntity.ok(alunos);
        });
    }
}
