package com.usic.conteo.model.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.dto.RecintoAgg;
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


      @Query(value = """
  SELECT
    r.nombre AS recinto,
    m.nombre AS municipio,
    p.nombre AS provincia,

    COALESCE(SUM(CASE WHEN vg.voto = 'PDC'    THEN dv.cantidad ELSE 0 END), 0) AS pdc,
    COALESCE(SUM(CASE WHEN vg.voto = 'LIBRE'  THEN dv.cantidad ELSE 0 END), 0) AS libre,
    COALESCE(SUM(CASE WHEN vg.voto = 'NULO'   THEN dv.cantidad ELSE 0 END), 0) AS nulo,
    COALESCE(SUM(CASE WHEN vg.voto = 'BLANCO' THEN dv.cantidad ELSE 0 END), 0) AS blanco,

    COALESCE(SUM(dv.cantidad), 0) AS total,

    /* habilitados viene de RECINTO (String):
       - si hay valores no numéricos, usa regexp_replace para limpiar
       - usamos MAX para no multiplicarlo por el join */
    MAX(
      COALESCE(
        NULLIF(regexp_replace(r.habilitados, '\\D', '', 'g'), '')::int,
        0
      )
    ) AS habilitados

  FROM recinto r
  JOIN municipio m  ON m.id_municipio = r.id_municipio
  JOIN provincia p  ON p.id_provincia = m.id_provincia
  LEFT JOIN mesa_general  mg ON mg.id_recinto     = r.id_recinto
  LEFT JOIN detalle_voto dv ON dv.id_mesa_general = mg.id_mesa_general
  LEFT JOIN voto_general  vg ON vg.id_voto_general = dv.id_voto_general

  GROUP BY r.nombre, m.nombre, p.nombre
  ORDER BY p.nombre, m.nombre, r.nombre
""", nativeQuery = true)
List<RecintoAgg> findRecintoAgg();
}
