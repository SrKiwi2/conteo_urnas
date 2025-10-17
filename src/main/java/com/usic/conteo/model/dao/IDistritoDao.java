package com.usic.conteo.model.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entityGeneral.Municipio;

public interface IDistritoDao extends JpaRepository<Municipio, Long> {
    
    @Query("SELECT d FROM Municipio d WHERE d.estado = 'ACTIVO'")
    List<Municipio> listarDistrito();

    @Query("SELECT r FROM Municipio r WHERE LOWER(r.nombre) = LOWER(?1) AND r.estado = 'ACTIVO'")
    Municipio buscarNombre(String nombre);

    List<Municipio> findByIdMunicipioIn(Collection<Long> ids);
}
