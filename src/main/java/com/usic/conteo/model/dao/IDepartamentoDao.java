package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entityGeneral.Departamento;

public interface IDepartamentoDao extends JpaRepository<Departamento, Long> {
    @Query("SELECT d FROM Departamento d WHERE d.estado = 'ACTIVO'")
    List<Departamento> listarDepartamentos();
}
