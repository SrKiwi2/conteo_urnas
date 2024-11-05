package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entity.Estudiante;

public interface IEstudianteDao extends JpaRepository <Estudiante, Long> {

    @Query("SELECT e FROM Estudiante e WHERE e.ru = ?1 AND e.estado = 'ACTIVO'")
    Estudiante buscarEstudinatePorRU(String ru);

    @Query("SELECT e FROM Estudiante e WHERE e.estado = 'ACTIVO'")
    List<Estudiante> listarEstudiantes();
}
