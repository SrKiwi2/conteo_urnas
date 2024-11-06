package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entity.Carrera;

public interface ICarreraDao extends JpaRepository<Carrera, Long>{
 
    @Query("SELECT c FROM Carrera c WHERE c.nombre_carrera = ?1 AND c.estado = 'ACTIVO'")
    Carrera buscarCarreraPorNombre(String nombre_carrera);

    @Query("SELECT c FROM Carrera c WHERE c.estado = 'ACTIVO'")
    List<Carrera> listarCarreras();
}
