package com.usic.conteo.model.IService;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.entity.Voto;

@Service
public interface IVotoService extends IServiceGenerico <Voto, Long>{

    List<Voto> listarVotosPorUsuario(@Param("idUsuario") Long id_usuario);
    List<Voto> listarVotos();
}
