package com.example.Professor.controller;

import com.example.Professor.kafka.KafkaProducer;
import com.example.Professor.model.Professor;
import com.example.Professor.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService serv;

    @Autowired
    private KafkaProducer producer;

    // Criar professor
    @PostMapping("/criarProfessor")
    public ResponseEntity<Professor> criar(@RequestBody Professor professor) {
        Professor salvo = serv.salvar(professor);
        String msg = "Novo professor cadastrado: " + salvo;
        producer.sendMessage("para-tecnico", msg);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping("/todos")
    public CompletableFuture<ResponseEntity<List<Professor>>> acharTodos() {
        var professores = serv.listar();
        String msg = "Listagem completa de professores" + professores.toString();
        producer.sendMessage("para-tecnico", msg);
        return professores.thenApply(ResponseEntity::ok);
    }


    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Professor> buscar(@PathVariable Long id) {
        Professor p = serv.buscar(id);
        if (p != null) {
            String msg = "Consulta de professor por ID " + id + ": " + p;
            producer.sendMessage("para-tecnico", msg);
            return ResponseEntity.ok(p);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Atualizar professor
    @PutMapping("/{id}")
    public ResponseEntity<Professor> atualizar(@PathVariable Long id, @RequestBody Professor professor) {
        Professor existente = serv.buscar(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        existente.setName(professor.getName());
        existente.setEmail(professor.getEmail());
        existente.setTurmas(professor.getTurmas());
        existente.setHorarios(professor.getHorarios());

        Professor atualizado = serv.salvar(existente);
        String msg = "Professor atualizado: " + atualizado;
        producer.sendMessage("para-tecnico", msg);
        return ResponseEntity.ok(atualizado);
    }

    // Deletar professor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Professor removido = serv.buscar(id);
        if (removido != null) {
            serv.deletar(id);
            String msg = "Professor removido: " + removido;
            producer.sendMessage("para-tecnico", msg);
        }
        return ResponseEntity.noContent().build();
    }

    // Adicionar turma
    @PostMapping("/{id}/turmas")
    public ResponseEntity<Professor> adicionarTurma(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String turma = body.get("turma");
        Professor atualizado = serv.adicionarTurma(id, turma);
        if (atualizado != null) {
            String msg = "Turma adicionada ao professor ID " + id + ": " + turma + " -> " + atualizado;
            producer.sendMessage("para-tecnico", msg);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // Remover turma
    @DeleteMapping("/{id}/turmas")
    public ResponseEntity<Professor> removerTurma(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String turma = body.get("turma");
        Professor atualizado = serv.removerTurma(id, turma);
        if (atualizado != null) {
            String msg = "Turma removida do professor ID " + id + ": " + turma + " -> " + atualizado;
            producer.sendMessage("para-tecnico", msg);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // Adicionar horário
    @PostMapping("/{id}/horarios")
    public ResponseEntity<Professor> adicionarHorario(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String horario = body.get("horario");
        Professor atualizado = serv.adicionarHorario(id, horario);
        if (atualizado != null) {
            String msg = "Horário adicionado ao professor ID " + id + ": " + horario + " -> " + atualizado;
            producer.sendMessage("para-tecnico", msg);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // Remover horário
    @DeleteMapping("/{id}/horarios")
    public ResponseEntity<Professor> removerHorario(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String horario = body.get("horario");
        Professor atualizado = serv.removerHorario(id, horario);
        if (atualizado != null) {
            String msg = "Horário removido do professor ID " + id + ": " + horario + " -> " + atualizado;
            producer.sendMessage("para-tecnico", msg);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // Envio manual de mensagem
    @PostMapping("/enviar-mensagem")
    public void enviarMensagem(@RequestParam String msg) {
        producer.sendMessage("para-tecnico", "Mensagem manual: " + msg);
    }
}
