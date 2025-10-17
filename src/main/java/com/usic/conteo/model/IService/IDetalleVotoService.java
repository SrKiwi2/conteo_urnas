package com.usic.conteo.model.IService;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.entityGeneral.DetalleVoto;

@Service
public interface IDetalleVotoService extends IServiceGenerico<DetalleVoto, Long>{
    List<DetalleVoto> listarDetalleVotos();
    Optional<DetalleVoto> findByMesaGeneralIdMesaGeneralAndVotoGeneralIdVotoGeneral(Long idMesa, Long idVoto);
    List<DetalleVoto> listarPorMesa(@Param("idMesa") Long idMesa);
}
