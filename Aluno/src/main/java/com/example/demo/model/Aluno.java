package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "senha_hash")
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cpf;
    private String name;
    private String senha_hash;
    private String email;
    private String curso;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "aluno_aulas", joinColumns = @JoinColumn(name = "aluno_id"))
    @Column(name = "nome_aula")
    private List<String> aulas;
}
