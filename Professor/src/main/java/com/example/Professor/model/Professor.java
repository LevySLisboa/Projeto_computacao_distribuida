package com.example.Professor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name = "professor",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email"}),
                @UniqueConstraint(columnNames = {"cpf"})
        })
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "senha_hash")
@Builder
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name, email, cpf, curso;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "senha_hash", nullable = false)
    private String senha_hash;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "professor_turmas", joinColumns = @JoinColumn(name = "professor_id"))
    @Column(name = "turma")
    private List<String> turmas;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "professor_horarios", joinColumns = @JoinColumn(name = "professor_id"))
    @Column(name = "horario")
    private List<String> horarios;
}
