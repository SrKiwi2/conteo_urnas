package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entity.Jurado;

public interface IJuradoDao extends JpaRepository<Jurado, Long> {
    
    @Query("SELECT j FROM Jurado j WHERE j.estado = 'ACTIVO'")
    List<Jurado> listarJurados();
}
