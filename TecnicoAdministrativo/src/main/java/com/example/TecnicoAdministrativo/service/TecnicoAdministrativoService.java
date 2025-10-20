package com.example.TecnicoAdministrativo.service;

import com.example.TecnicoAdministrativo.model.TecnicoAdministrativo;
import com.example.TecnicoAdministrativo.repository.TecnicoAdministrativoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TecnicoAdministrativoService {
    @Autowired
    private TecnicoAdministrativoRepository repo;

    public TecnicoAdministrativo salvar(TecnicoAdministrativo t){return repo.save(t);}

    public List<TecnicoAdministrativo> listar(){return repo.findAll();}

    public TecnicoAdministrativo buscar(Long id){return repo.findById(id).orElse(null);}

    public void deletar(Long id){repo.deleteById(id);}

}
