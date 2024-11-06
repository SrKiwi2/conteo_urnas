package com.usic.conteo.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entity.Voto;

@Service
public interface IVotoService extends IServiceGenerico <Voto, Long>{
    List<Voto> listarVotos();
}
