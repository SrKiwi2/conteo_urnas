package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entity.Frente;

public interface IFrenteDao extends JpaRepository <Frente, Long> {

    @Query("SELECT f FROM Frente f WHERE f.estado = 'ACTIVO'")
    List<Frente> listarFrentes();
}
