package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entity.Voto;

public interface IVotoDao extends JpaRepository<Voto, Long> {
    
    @Query("SELECT v FROM Voto v WHERE v.estado = 'ACTIVO'")
    List<Voto> listarVotos();
}