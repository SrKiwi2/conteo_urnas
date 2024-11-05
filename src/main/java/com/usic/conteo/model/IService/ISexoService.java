package com.usic.conteo.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entity.Sexo;

@Service
public interface ISexoService extends IServiceGenerico<Sexo, Long>{
    Sexo buscarSexoPorNombre(String nombre);
    List<Sexo> listarGeneros();
}
