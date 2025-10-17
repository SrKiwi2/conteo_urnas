package com.usic.conteo.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entityGeneral.Municipio;
import com.usic.conteo.model.entityGeneral.Recinto;

public interface IRecintoDao extends JpaRepository<Recinto, Long> {

    @Query("SELECT r FROM Recinto r WHERE r.estado = 'ACTIVO'")
    List<Recinto> listarRecintos();

    @Query("SELECT r FROM Recinto r WHERE LOWER(r.nombre) = LOWER(?1) AND r.estado = 'ACTIVO'")
    Recinto buscarNombre(String nombre);

    Optional<Recinto> findByNombreAndMunicipio(String nombre, Municipio municipio);
    Optional<Recinto> findByNombreAndMunicipio_IdMunicipio(String nombre, Long municipioId);
}
