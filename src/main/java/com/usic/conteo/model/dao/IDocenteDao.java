package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entity.Docente;

public interface IDocenteDao extends JpaRepository<Docente, Long> {
    @Query("SELECT d FROM Docente d WHERE d.rd = ?1 AND d.estado = 'ACTIVO'")
    Docente buscarDocentePorRD(String rd);
    
    @Query("SELECT d FROM Docente d WHERE d.estado = 'ACTIVO'")
    List<Docente> listarDocentes();    
}
