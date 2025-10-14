package com.example.TecnicoAdministrativo.repository;

import com.example.TecnicoAdministrativo.model.TecnicoAdministrativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TecnicoAdministrativoRepository extends JpaRepository<TecnicoAdministrativo,Long> {
}
