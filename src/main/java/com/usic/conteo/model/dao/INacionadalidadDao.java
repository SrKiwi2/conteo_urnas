package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entity.Nacionalidad;

public interface INacionadalidadDao extends JpaRepository<Nacionalidad, Long>{
    
    @Query("SELECT n FROM Nacionalidad n WHERE n.nombre = ?1 AND n.estado = 'ACTIVO'")
    Nacionalidad buscarNacionalidadPorNombre(String nombre);

    @Query("SELECT n FROM Estudiante n WHERE n.estado = 'ACTIVO'")
    List<Nacionalidad> listarNacionalidades();
}
