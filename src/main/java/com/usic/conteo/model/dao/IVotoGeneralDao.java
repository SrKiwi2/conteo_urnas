package com.usic.conteo.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entityGeneral.VotoGeneral;

public interface IVotoGeneralDao extends JpaRepository<VotoGeneral, Long>{
    
    @Query("SELECT vg FROM VotoGeneral vg WHERE vg.estado = 'ACTIVO'")
    List<VotoGeneral> listarVotoGeneral();

    Optional<VotoGeneral> findByVotoIgnoreCase(String voto);
}