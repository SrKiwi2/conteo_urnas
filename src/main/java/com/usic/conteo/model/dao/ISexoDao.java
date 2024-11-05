package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entity.Sexo;

public interface ISexoDao extends JpaRepository<Sexo, Long>{
    
    @Query("SELECT s FROM Sexo s WHERE s.nombre = ?1 AND s.estado = 'ACTIVO'")
    Sexo buscarSexoPorNombre(String nombre);

    @Query("SELECT s FROM Sexo s WHERE s.estado = 'ACTIVO'")
    List<Sexo> listarGeneros();
}
