package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.usic.conteo.model.entity.Voto;

public interface IVotoDao extends JpaRepository<Voto, Long> {
    
    @Query("SELECT v FROM Voto v " +
       "JOIN v.mesa m " +
       "JOIN m.jurado j " +
       "JOIN Usuario u ON u.jurado = j " +
       "WHERE u.idUsuario = :idUsuario")
    List<Voto> listarVotosPorUsuario(@Param("idUsuario") Long id_usuario);


    @Query("SELECT v FROM Voto v WHERE v.estado = 'ACTIVO'")
    List<Voto> listarVotos();

    
}

