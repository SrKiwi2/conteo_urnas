package com.usic.conteo.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entity.Jurado;
import com.usic.conteo.model.entity.Persona;

public interface IJuradoDao extends JpaRepository<Jurado, Long> {
    @Query("SELECT j FROM Jurado j WHERE j.persona = ?1 AND j.estado = 'ACTIVO'")
    Optional<Jurado> buscarJuradoPorPersona(Persona persona);

    @Query("SELECT j FROM Jurado j WHERE j.estado = 'ACTIVO'")
    List<Jurado> listarJurados();
}
