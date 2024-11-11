package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.usic.conteo.model.entity.Mesa;

public interface IMesaDao extends JpaRepository<Mesa, Long> {

    @Query("SELECT m FROM Mesa m WHERE m.estado = 'ACTIVO'")
    List<Mesa> listarMesas();
}

