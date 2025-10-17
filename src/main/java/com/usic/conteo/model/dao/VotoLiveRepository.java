package com.usic.conteo.model.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entityGeneral.Recinto;

public interface VotoLiveRepository extends JpaRepository<Recinto, Long> {
    @Query(value = """
            SELECT
                id_recinto          AS idRecinto,
                recinto_nombre      AS recinto,
                id_municipio        AS idMunicipio,
                municipio_nombre    AS municipio,
                id_provincia        AS idProvincia,
                provincia_nombre    AS provincia,
                pdc, libre, nulo, blanco,
                total_votos         AS total,
                habilitados,
                participacion_pct   AS participacionPct
                FROM vw_votos_recinto
            """, nativeQuery = true)
    List<Map<String, Object>> findAllAggRaw(); // o proyéctalo a interface

    @Query(value = """
              SELECT provincia_nombre AS provincia,
                     SUM(pdc)    AS pdc,    SUM(libre)  AS libre,
                     SUM(nulo)   AS nulo,   SUM(blanco) AS blanco,
                     SUM(total_votos) AS total,
                     SUM(habilitados) AS habilitados
              FROM vw_votos_recinto
              GROUP BY provincia_nombre
              ORDER BY provincia_nombre
            """, nativeQuery = true)
    List<Map<String, Object>> sumByProvincia();

    @Query(value = """
            SELECT municipio_nombre AS municipio,
                    SUM(pdc)    AS pdc,    SUM(libre)  AS libre,
                    SUM(nulo)   AS nulo,   SUM(blanco) AS blanco,
                    SUM(total_votos) AS total,
                    SUM(habilitados) AS habilitados
            FROM vw_votos_recinto
            GROUP BY municipio_nombre
            ORDER BY total DESC
            """, countQuery = """
            SELECT COUNT(DISTINCT municipio_nombre)
            FROM vw_votos_recinto
            """, nativeQuery = true)
    List<Map<String, Object>> topMunicipios(Pageable pageable);
}
