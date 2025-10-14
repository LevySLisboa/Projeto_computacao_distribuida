package com.example.Usuario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name,email,senhahash,classe;
    @ElementCollection
    @CollectionTable(name = "usuario_funcao", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "funcao")
    private List<String> funcoes;
}
