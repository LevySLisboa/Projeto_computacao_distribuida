package com.example.Professor.controller;

import com.example.Professor.kafka.KafkaProducer;
import com.example.Professor.model.Professor;
import com.example.Professor.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/professor")
public class ProfessorController {
    @Autowired
    private ProfessorService serv;

    @Autowired
    private KafkaProducer producer;

    @PostMapping("/criarProfessor")
    public ResponseEntity<Professor> criar(@RequestBody Professor professor) {
        Professor salvo = serv.salvar(professor);
        return ResponseEntity.ok(salvo);
    }

    // Listar todos
    @GetMapping("/todos")
    public ResponseEntity<List<Professor>> listar() {
        return ResponseEntity.ok(serv.listar());
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Professor> buscar(@PathVariable Long id) {
        Professor p = serv.buscar(id);
        return (p != null) ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Professor> atualizar(@PathVariable Long id, @RequestBody Professor professor) {
        Professor existente = serv.buscar(id);
        if (existente == null) return ResponseEntity.notFound().build();

        existente.setName(professor.getName());
        existente.setEmail(professor.getEmail());
        existente.setTurmas(professor.getTurmas());
        existente.setHorarios(professor.getHorarios());

        Professor atualizado = serv.salvar(existente);
        return ResponseEntity.ok(atualizado);
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        serv.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Adicionar turma
    @PostMapping("/{id}/turmas")
    public ResponseEntity<Professor> adicionarTurma(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String turma = body.get("turma");
        Professor atualizado = serv.adicionarTurma(id, turma);
        return (atualizado != null) ? ResponseEntity.ok(atualizado) : ResponseEntity.notFound().build();
    }

    // Remover turma
    @DeleteMapping("/{id}/turmas")
    public ResponseEntity<Professor> removerTurma(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String turma = body.get("turma");
        Professor atualizado = serv.removerTurma(id, turma);
        return (atualizado != null) ? ResponseEntity.ok(atualizado) : ResponseEntity.notFound().build();
    }

    // Adicionar horário
    @PostMapping("/{id}/horarios")
    public ResponseEntity<Professor> adicionarHorario(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String horario = body.get("horario");
        Professor atualizado = serv.adicionarHorario(id, horario);
        return (atualizado != null) ? ResponseEntity.ok(atualizado) : ResponseEntity.notFound().build();
    }

    // Remover horário
    @DeleteMapping("/{id}/horarios")
    public ResponseEntity<Professor> removerHorario(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String horario = body.get("horario");
        Professor atualizado = serv.removerHorario(id, horario);
        return (atualizado != null) ? ResponseEntity.ok(atualizado) : ResponseEntity.notFound().build();
    }

    @PostMapping("/enviar-mensagem")
    public void enviarMensagem(@RequestParam String msg) {
        producer.sendMessage("notificacoes", msg);
    }
}
