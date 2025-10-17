package com.usic.conteo.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.usic.conteo.model.entityGeneral.DetalleVoto;

public interface IDetalleVotoDao extends JpaRepository<DetalleVoto, Long> {
    @Query("SELECT dv FROM DetalleVoto dv WHERE dv.estado = 'ACTIVO'")
    List<DetalleVoto> listarDetalleVotos();

    Optional<DetalleVoto> findByMesaGeneralIdMesaGeneralAndVotoGeneralIdVotoGeneral(Long idMesa, Long idVoto);

    @Query("select dv from DetalleVoto dv " +
           "join fetch dv.mesaGeneral mg " +
           "join fetch dv.votoGeneral vg " +
           "where mg.idMesaGeneral = :idMesa")
    List<DetalleVoto> listarPorMesa(@Param("idMesa") Long idMesa);

    @Query("""
        select upper(vg.voto) as tipo, coalesce(sum(dv.cantidad),0)
        from DetalleVoto dv
        join dv.votoGeneral vg
        group by upper(vg.voto)
    """)
    List<Object[]> sumarPorTipo();
}
