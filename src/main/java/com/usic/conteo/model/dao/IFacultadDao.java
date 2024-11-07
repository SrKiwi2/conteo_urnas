package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.usic.conteo.model.entity.Facultad;

public interface IFacultadDao extends JpaRepository<Facultad, Long>{

    @Query("SELECT f FROM Facultad f WHERE f.estado = 'ACTIVO'")
    List<Facultad> listarFacultades();

    @Query("SELECT f FROM Facultad f WHERE f.nombre_facultad = ?1 AND f.estado = 'ACTIVO'")
    Facultad buscarFacultad(String nombre_facultad);
    
}
