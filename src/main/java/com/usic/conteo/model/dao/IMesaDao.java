package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.usic.conteo.model.entity.Mesa;

public interface IMesaDao extends JpaRepository<Mesa, Long> {

    @Query("SELECT m FROM Mesa m WHERE m.estado = 'ACTIVO' order by m.id_mesa asc")
    List<Mesa> listarMesas();

    @Query(value = """
        SELECT 
            (COALESCE(m.habilitados, 0) - COALESCE(SUM(CAST(v.cantidad AS INT)), 0)) AS restantes, COALESCE(SUM(CAST(v.cantidad AS INT)), 0) AS registrados
        FROM 
            mesa m
        LEFT JOIN 
            voto v ON m.id_mesa = v.id_mesa where m.id_mesa = ?1
        GROUP BY 
            m.id_mesa
        """, nativeQuery = true)
    List<Object[]> findMesasWithRestantes(Long id_mesa);

    @Query("SELECT m FROM Mesa m WHERE m.carrera.id_carrera = ?1 AND m.estado = 'ACTIVO' order by m.id_mesa asc")
    List<Mesa> findByCarrera(@Param("idCarrera") Long idCarrera);
}

