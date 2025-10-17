package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entityGeneral.Provincia;

public interface IProvinciaDao extends JpaRepository<Provincia, Long> {
    @Query("SELECT p FROM Provincia p WHERE p.estado = 'ACTIVO'")
    List<Provincia> listarProvincias();
}
