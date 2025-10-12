package com.example.Professor.service;

import com.example.Professor.model.Professor;
import com.example.Professor.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository repo;

    public Professor salvar(Professor professor) {
        return repo.save(professor);
    }

    public List<Professor> listar() {
        return repo.findAll();
    }

    public Professor buscar(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }

    public Professor adicionarTurma(Long professorId, String turma) {
        Professor p = buscar(professorId);
        if (p == null) return null;

        if (!p.getTurmas().contains(turma)) {
            p.getTurmas().add(turma);
        }
        return repo.save(p);
    }

    public Professor removerTurma(Long professorId, String turma) {
        Professor p = buscar(professorId);
        if (p == null) return null;

        p.getTurmas().remove(turma);
        return repo.save(p);
    }

    public Professor adicionarHorario(Long professorId, String horario) {
        Professor p = buscar(professorId);
        if (p == null) return null;

        if (!p.getHorarios().contains(horario)) {
            p.getHorarios().add(horario);
        }
        return repo.save(p);
    }

    public Professor removerHorario(Long professorId, String horario) {
        Professor p = buscar(professorId);
        if (p == null) return null;

        p.getHorarios().remove(horario);
        return repo.save(p);
    }
}

