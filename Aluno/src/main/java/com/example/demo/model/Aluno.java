package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name = "aluno",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"cpf"}),
                @UniqueConstraint(columnNames = {"email"})
        })
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "senha_hash")
@Builder
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cpf;
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "senha_hash", nullable = false)

    private String senha_hash;
    private String email;
    private String curso;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "aluno_aulas", joinColumns = @JoinColumn(name = "aluno_id"))
    @Column(name = "nome_aula")
    private List<String> aulas;
}
