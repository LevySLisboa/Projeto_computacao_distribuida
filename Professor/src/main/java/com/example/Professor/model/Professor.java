package com.example.Professor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "senha_hash")
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name,email,senha_hash;
    @ElementCollection
    @CollectionTable(name = "professor_turmas", joinColumns = @JoinColumn(name = "professor_id"))
    @Column(name = "turma")
    private List<String> turmas;

    // Lista de horários como strings
    @ElementCollection
    @CollectionTable(name = "professor_horarios", joinColumns = @JoinColumn(name = "professor_id"))
    @Column(name = "horario")
    private List<String> horarios;
}
