package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entity.Persona;

public interface IPersonaDao extends JpaRepository <Persona, Long>{
    
    @Query("SELECT p FROM Persona p WHERE p.ci = ?1 AND p.estado = 'ACTIVO'")
    Persona buscarPersonaPorCI(String ci);

    @Query("SELECT p FROM Persona p WHERE p.estado = 'ACTIVO'")
    List<Persona> listarPersonas();
}
