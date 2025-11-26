package com.example.GestaoTurmas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Turma {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private Long professorId;
        private String horario;
        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(name = "turma_alunos", joinColumns = @JoinColumn(name = "turma_id"))
        @Column(name = "aluno_id")
        private List<Long> alunosIds;
}
