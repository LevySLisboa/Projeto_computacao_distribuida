package com.example.Professor.controller;

import com.example.Professor.kafka.KafkaProducer;
import com.example.Professor.model.NotificacaoEvento;
import com.example.Professor.model.Professor;
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

        NotificacaoEvento evento = NotificacaoEvento.builder()
                .tipo("CRIACAO")
                .destino("tecnico")
                .mensagem("Novo professor cadastrado: " + salvo)
                .timestamp(System.currentTimeMillis())
                .build();

        producer.sendEvent(TOPICO_TECNICO, evento);
        log.info("Professor criado e evento enviado: {}", evento);
        return ResponseEntity.ok(salvo);
    }

    // 📚 Listar todos (assíncrono)
    @GetMapping("/todos")
    public CompletableFuture<ResponseEntity<List<Professor>>> acharTodos() {
        return serv.listar().thenApply(professores -> {
            NotificacaoEvento evento = NotificacaoEvento.builder()
                    .tipo("LISTAGEM")
                    .destino("tecnico")
                    .mensagem("Listagem completa de professores: " + professores)
                    .timestamp(System.currentTimeMillis())
                    .build();

            producer.sendEvent(TOPICO_TECNICO, evento);
            log.info("Evento de listagem enviado: {}", evento);

            return ResponseEntity.ok(professores);
        });
    }

    // 🔍 Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Professor> buscar(@PathVariable Long id) {
        log.info("Solicitação para buscar professor ID {}", id);
        Professor p = serv.buscar(id);

        if (p != null) {
            NotificacaoEvento evento = NotificacaoEvento.builder()
                    .tipo("BUSCA_PROFESSOR")
                    .destino("tecnico")
                    .mensagem("Consulta de professor por ID " + id + ": " + p)
                    .timestamp(System.currentTimeMillis())
                    .build();

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
        existente.setTurmas(professor.getTurmas());
        existente.setHorarios(professor.getHorarios());

        Professor atualizado = serv.salvar(existente);

        NotificacaoEvento evento = NotificacaoEvento.builder()
                .tipo("ATUALIZAR_PROFESSOR")
                .destino("tecnico")
                .mensagem("Professor atualizado: " + atualizado)
                .timestamp(System.currentTimeMillis())
                .build();

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

            NotificacaoEvento evento = NotificacaoEvento.builder()
                    .tipo("DELETAR_PROFESSOR")
                    .destino("tecnico")
                    .mensagem("Professor removido: " + removido)
                    .timestamp(System.currentTimeMillis())
                    .build();

            producer.sendEvent(TOPICO_TECNICO, evento);
            log.info("Evento de exclusão enviado: {}", evento);
        } else {
            log.warn("Tentativa de remover professor inexistente ID {}", id);
        }

        return ResponseEntity.noContent().build();
    }

    // ➕ Adicionar turma
    @PostMapping("/{id}/turmas")
    public ResponseEntity<Professor> adicionarTurma(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String turma = body.get("turma");
        log.info("Solicitação para adicionar turma '{}' ao professor ID {}", turma, id);

        Professor atualizado = serv.adicionarTurma(id, turma);
        if (atualizado != null) {
            NotificacaoEvento evento = NotificacaoEvento.builder()
                    .tipo("ADICIONAR_TURMA")
                    .destino("tecnico")
                    .mensagem("Turma adicionada ao professor ID " + id + ": " + turma)
                    .timestamp(System.currentTimeMillis())
                    .build();

            producer.sendEvent(TOPICO_TECNICO, evento);
            log.info("Evento de adição de turma enviado: {}", evento);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // ➖ Remover turma
    @DeleteMapping("/{id}/turmas")
    public ResponseEntity<Professor> removerTurma(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String turma = body.get("turma");
        log.info("Solicitação para remover turma '{}' do professor ID {}", turma, id);

        Professor atualizado = serv.removerTurma(id, turma);
        if (atualizado != null) {
            NotificacaoEvento evento = NotificacaoEvento.builder()
                    .tipo("REMOVER_TURMA")
                    .destino("tecnico")
                    .mensagem("Turma removida do professor ID " + id + ": " + turma)
                    .timestamp(System.currentTimeMillis())
                    .build();

            producer.sendEvent(TOPICO_TECNICO, evento);
            log.info("Evento de remoção de turma enviado: {}", evento);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // ⏰ Adicionar horário
    @PostMapping("/{id}/horarios")
    public ResponseEntity<Professor> adicionarHorario(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String horario = body.get("horario");
        log.info("Solicitação para adicionar horário '{}' ao professor ID {}", horario, id);

        Professor atualizado = serv.adicionarHorario(id, horario);
        if (atualizado != null) {
            NotificacaoEvento evento = NotificacaoEvento.builder()
                    .tipo("ADICIONAR_HORARIO")
                    .destino("tecnico")
                    .mensagem("Horário adicionado ao professor ID " + id + ": " + horario)
                    .timestamp(System.currentTimeMillis())
                    .build();

            producer.sendEvent(TOPICO_TECNICO, evento);
            log.info("Evento de adição de horário enviado: {}", evento);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // 🗑️ Remover horário
    @DeleteMapping("/{id}/horarios")
    public ResponseEntity<Professor> removerHorario(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String horario = body.get("horario");
        log.info("Solicitação para remover horário '{}' do professor ID {}", horario, id);

        Professor atualizado = serv.removerHorario(id, horario);
        if (atualizado != null) {
            NotificacaoEvento evento = NotificacaoEvento.builder()
                    .tipo("REMOVER_HORARIO")
                    .destino("tecnico")
                    .mensagem("Horário removido do professor ID " + id + ": " + horario)
                    .timestamp(System.currentTimeMillis())
                    .build();

            producer.sendEvent(TOPICO_TECNICO, evento);
            log.info("Evento de remoção de horário enviado: {}", evento);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // 📩 Envio manual de mensagem
    @PostMapping("/enviar-mensagem")
    public ResponseEntity<String> enviarMensagem(@RequestParam String msg) {
        NotificacaoEvento evento = NotificacaoEvento.builder()
                .tipo("MENSAGEM_MANUAL")
                .destino("tecnico")
                .mensagem("Mensagem manual: " + msg)
                .timestamp(System.currentTimeMillis())
                .build();

        producer.sendEvent(TOPICO_TECNICO, evento);
        log.info("Evento de mensagem manual enviado: {}", evento);

        return ResponseEntity.ok("Mensagem manual enviada com sucesso!");
    }
}
