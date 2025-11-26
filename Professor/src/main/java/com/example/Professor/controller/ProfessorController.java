package com.example.Professor.controller;

import com.example.Professor.kafka.KafkaProducer;
import com.example.Professor.model.NotificacaoEvento;
import com.example.Professor.model.Professor;
import com.example.Professor.model.dto.ProfessorDetalheDTO;
import com.example.Professor.service.ProfessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService serv;

    @Autowired
    private KafkaProducer producer;



    private static final String TOPICO_TECNICO = "para-tecnico";

    // 🧩 Criar professor
    @PostMapping("/criarProfessor")
    public ResponseEntity<Professor> criar(@RequestBody Professor professor) {
        Professor salvo = serv.salvar(professor);

        NotificacaoEvento evento = NotificacaoEvento.builder().tipo("CRIACAO").destino("tecnico").mensagem("Novo professor cadastrado: " + salvo).timestamp(System.currentTimeMillis()).build();

        producer.sendEvent(TOPICO_TECNICO, evento);
        log.info("Professor criado e evento enviado: {}", evento);
        return ResponseEntity.ok(salvo);
    }

    // 📚 Listar todos (assíncrono)
    @GetMapping("/todos")
    public CompletableFuture<ResponseEntity<List<Professor>>> acharTodos() {
        return serv.listar().thenApply(professores -> {
            NotificacaoEvento evento = NotificacaoEvento.builder().tipo("LISTAGEM").destino("tecnico").mensagem("Listagem completa de professores: " + professores).timestamp(System.currentTimeMillis()).build();

            producer.sendEvent(TOPICO_TECNICO, evento);
            log.info("Evento de listagem enviado: {}", evento);

            return ResponseEntity.ok(professores);
        });
    }

    // 🔍 Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDetalheDTO> buscar(@PathVariable Long id) {
        log.info("Solicitação para buscar professor ID {}", id);
        ProfessorDetalheDTO p = serv.buscarComTurmas(id);

        if (p != null) {
            NotificacaoEvento evento = NotificacaoEvento.builder().tipo("BUSCA_PROFESSOR").destino("tecnico").mensagem("Consulta de professor por ID " + id + ": " + p).timestamp(System.currentTimeMillis()).build();

            producer.sendEvent(TOPICO_TECNICO, evento);
            log.info("Evento de busca enviado: {}", evento);
            return ResponseEntity.ok(p);
        } else {
            log.warn("Professor ID {} não encontrado", id);
            return ResponseEntity.notFound().build();
        }
    }

    // 🔄 Atualizar professor
    @PutMapping("/{id}")
    public ResponseEntity<Professor> atualizar(@PathVariable Long id, @RequestBody Professor professor) {
        log.info("Solicitação para atualizar professor ID {}", id);
        Professor existente = serv.buscar(id);

        if (existente == null) {
            log.warn("Professor ID {} não encontrado para atualização", id);
            return ResponseEntity.notFound().build();
        }

        existente.setName(professor.getName());
        existente.setEmail(professor.getEmail());
        existente.setTurmasIds(professor.getTurmasIds());

        Professor atualizado = serv.salvar(existente);

        NotificacaoEvento evento = NotificacaoEvento.builder().tipo("ATUALIZAR_PROFESSOR").destino("tecnico").mensagem("Professor atualizado: " + atualizado).timestamp(System.currentTimeMillis()).build();

        producer.sendEvent(TOPICO_TECNICO, evento);
        log.info("Evento de atualização enviado: {}", evento);

        return ResponseEntity.ok(atualizado);
    }

    // 🗑️ Deletar professor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Solicitação para deletar professor ID {}", id);
        Professor removido = serv.buscar(id);

        if (removido != null) {
            serv.deletar(id);

            NotificacaoEvento evento = NotificacaoEvento.builder().tipo("DELETAR_PROFESSOR").destino("tecnico").mensagem("Professor removido: " + removido).timestamp(System.currentTimeMillis()).build();

            producer.sendEvent(TOPICO_TECNICO, evento);
            log.info("Evento de exclusão enviado: {}", evento);
        } else {
            log.warn("Tentativa de remover professor inexistente ID {}", id);
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{profId}/turmas/{turmaId}")
    public ResponseEntity<Professor> adicionarTurma(
            @PathVariable Long profId,
            @PathVariable Long turmaId) {

        log.info("Adicionando turma {} ao professor {}", turmaId, profId);
        NotificacaoEvento evento = NotificacaoEvento.builder().tipo("ADICIONAR_TURMA").destino("tecnico").mensagem("Turma ID " + turmaId + " adicionada ao professor ID " + profId).timestamp(System.currentTimeMillis()).build();
        producer.sendEvent(TOPICO_TECNICO, evento);

        Professor professor = serv.adicionarTurma(profId, turmaId);

        return ResponseEntity.ok(professor);
    }


    @DeleteMapping("/{id}/turmas")
    public ResponseEntity<Professor> removerTurma(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Long turmaId = body.get("turmaId");
        log.info("Solicitação para remover a turma ID={} do professor ID={}", turmaId, id);

        Professor atualizado = serv.removerTurma(id, turmaId);

        NotificacaoEvento evento = NotificacaoEvento.builder().tipo("REMOVER_TURMA").destino("tecnico").mensagem("Turma ID " + turmaId + " removida do professor ID " + id).timestamp(System.currentTimeMillis()).build();

        producer.sendEvent(TOPICO_TECNICO, evento);
        log.info("Evento de remoção de turma enviado: {}", evento);

        return ResponseEntity.ok(atualizado);
    }


    // 📩 Envio manual de mensagem
    @PostMapping("/enviar-mensagem")
    public ResponseEntity<String> enviarMensagem(@RequestParam String msg) {
        NotificacaoEvento evento = NotificacaoEvento.builder().tipo("MENSAGEM_MANUAL").destino("tecnico").mensagem("Mensagem manual: " + msg).timestamp(System.currentTimeMillis()).build();

        producer.sendEvent(TOPICO_TECNICO, evento);
        log.info("Evento de mensagem manual enviado: {}", evento);

        return ResponseEntity.ok("Mensagem manual enviada com sucesso!");
    }
}
