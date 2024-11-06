package com.usic.conteo.model.IService;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entity.Jurado;
import com.usic.conteo.model.entity.Persona;

@Service
public interface IJuradoService extends IServiceGenerico<Jurado, Long> {
    List<Jurado> listarJurados();
    Optional<Jurado> buscarJuradoPorPersona(Persona persona);
}
