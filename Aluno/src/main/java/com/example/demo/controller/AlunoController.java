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
    public ResponseEntity<Aluno> criarAluno (@RequestBody Aluno aluno){
        return ResponseEntity.ok().body(serv.criarALuno(aluno));
    }
    @PostMapping("/atualizarAluno")
    public ResponseEntity<Aluno> atualizarAluno (@RequestBody Aluno aluno){
        return ResponseEntity.ok().body(serv.atualizarAluno(aluno));
    }
    @DeleteMapping(value = "/{matricula}")
    public ResponseEntity<?> deletarAluno(@PathVariable(value = "matricula") Long matricula){
        serv.deletarAluno(matricula);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{matricula}")
    public ResponseEntity<Aluno> acharPorMatricula (@PathVariable(value = "matricula") Long matricula){
        return ResponseEntity.ok().body(serv.acharPorMatricula(matricula));
    }
    @GetMapping("/todos")
    public ResponseEntity<List<Aluno>> acharTodos (){
        return ResponseEntity.ok().body(serv.acharTodos());
    }
    @PostMapping("/enviar-mensagem")
    public void enviarMensagem(@RequestParam String msg) {
        producer.sendMessage("para-professor", msg);
    }
}
