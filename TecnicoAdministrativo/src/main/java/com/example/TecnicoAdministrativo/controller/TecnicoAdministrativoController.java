package com.example.TecnicoAdministrativo.controller;

import com.example.TecnicoAdministrativo.model.TecnicoAdministrativo;
import com.example.TecnicoAdministrativo.service.TecnicoAdministrativoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tecnicoadministrativo")
public class TecnicoAdministrativoController {
    @Autowired
    private TecnicoAdministrativoService service;

    @PostMapping("/criarTecnicoAdministrativo")
    public ResponseEntity<TecnicoAdministrativo> criar(@RequestBody TecnicoAdministrativo tecnicoAdministrativo){
        TecnicoAdministrativo salvo = service.salvar(tecnicoAdministrativo);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping ("/todos")
    public ResponseEntity<List<TecnicoAdministrativo>> buscarTodos(){
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoAdministrativo> buscar(@PathVariable Long id) {
        TecnicoAdministrativo p = service.buscar(id);
        return (p != null) ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
