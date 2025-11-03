package com.example.demo.controller;

import com.example.demo.kafka.KafkaProducer;
import com.example.demo.model.Aluno;
import com.example.demo.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

    @Autowired
    private AlunoService serv;

    @Autowired
    private KafkaProducer producer;

    @PostMapping("/criarAluno")
    public ResponseEntity<Aluno> criarAluno(@RequestBody Aluno aluno) {
        Aluno salvo = serv.criarALuno(aluno);
        String msg = "Novo aluno cadastrado: " + salvo;
        producer.sendMessage("para-tecnico", msg);
        return ResponseEntity.ok().body(salvo);
    }
    @PostMapping("/atualizarAluno")
    public ResponseEntity<Aluno> atualizarAluno(@RequestBody Aluno aluno) {
        Aluno atualizado = serv.atualizarAluno(aluno);
        String msg = "Aluno atualizado: " + atualizado;
        producer.sendMessage("para-tecnico", msg);
        return ResponseEntity.ok().body(atualizado);
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> deletarAluno(@PathVariable("matricula") Long matricula) {
        Aluno removido = serv.acharPorMatricula(matricula);
        serv.deletarAluno(matricula);
        String msg = "Aluno removido: " + removido;
        producer.sendMessage("para-tecnico", msg);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<Aluno> acharPorMatricula(@PathVariable("matricula") Long matricula) {
        Aluno aluno = serv.acharPorMatricula(matricula);
        String msg = "Consulta de aluno por matrícula " + matricula + ": " + aluno;
        producer.sendMessage("para-tecnico", msg);
        return ResponseEntity.ok().body(aluno);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Aluno>> acharTodos() {
        List<Aluno> alunos = serv.acharTodos();
        StringBuilder msg = new StringBuilder("Listagem completa de alunos (" + alunos.size() + " registros): ");
        for(Aluno a :alunos){
            msg.append(a+"\n");
        }
        producer.sendMessage("para-tecnico", msg.toString());
        return ResponseEntity.ok().body(alunos);
    }
}
