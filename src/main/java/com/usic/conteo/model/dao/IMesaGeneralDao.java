package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entityGeneral.MesaGeneral;

public interface IMesaGeneralDao extends JpaRepository<MesaGeneral, Long>{
    
    @Query("SELECT mg FROM MesaGeneral mg WHERE mg.estado = 'ACTIVO'")
    List<MesaGeneral> listarMesaGeneral();

    // ¿Existe una mesa con ese número en el recinto?
    boolean existsByNumeroMesaIgnoreCaseAndRecinto_IdRecinto(String numeroMesa, Long idRecinto);

    // ¿Existe otra mesa (distinta a idMesaGeneral) con ese número en el recinto?
    boolean existsByNumeroMesaIgnoreCaseAndRecinto_IdRecintoAndIdMesaGeneralNot(
            String numeroMesa, Long idRecinto, Long idMesaGeneral);

    @Query("""
        select m
        from MesaGeneral m
        where not exists (
           select 1 from DetalleVoto dv
           where dv.mesaGeneral = m
        )
        order by m.recinto.nombre asc, m.numeroMesa asc
    """)
    List<MesaGeneral> listarMesasSinResultados();
}
