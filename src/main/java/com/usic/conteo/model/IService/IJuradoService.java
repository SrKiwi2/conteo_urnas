package com.usic.conteo.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entity.Jurado;

@Service
public interface IJuradoService extends IServiceGenerico<Jurado, Long> {
    List<Jurado> listarJurados();
}
