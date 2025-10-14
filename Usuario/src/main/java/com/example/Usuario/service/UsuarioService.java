package com.example.Usuario.service;

import com.example.Usuario.model.Usuario;
import com.example.Usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repo;
    public Usuario salvar(Usuario usuario){return repo.save(usuario);}
    public List<Usuario> listar(Usuario usuario){return repo.findAll();}
    public Usuario buscar(long id){return repo.findById(id).orElse(null);}
    public void deletar(long id){repo.deleteById(id);}
}
