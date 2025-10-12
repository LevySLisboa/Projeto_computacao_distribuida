package com.example.demo.service;

import com.example.demo.model.Aluno;
import com.example.demo.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {
    @Autowired
    private AlunoRepository repo;

    public Aluno criarALuno(Aluno aluno){
        return repo.save(aluno);
    }
    public List<Aluno> acharTodos(){
        return repo.findAll();
    }
    public Aluno acharPorMatricula(Long matricula){
        return repo.findById(matricula).orElseThrow();
    }
    public void deletarAluno (Long matricula){
        Aluno aluno = acharPorMatricula(matricula);
        repo.delete(aluno);
    }
    public Aluno atualizarAluno (Aluno newAluno){
        Aluno aluno= repo.findById(newAluno.getId()).orElseThrow();
        aluno.setName(newAluno.getName());
        aluno.setCurso(newAluno.getCurso());
        aluno.setEmail(newAluno.getEmail());
        aluno.setAulas(newAluno.getAulas());
        return repo.save(aluno);
    }
}
