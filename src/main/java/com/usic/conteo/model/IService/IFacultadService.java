package com.usic.conteo.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entity.Facultad;

@Service
public interface IFacultadService extends IServiceGenerico<Facultad, Long> {
    Facultad buscarFacultadPorNombre(String nombre_facultad);
    List<Facultad> listarFacultades();
}
