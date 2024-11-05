package com.usic.conteo.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entity.Nacionalidad;

@Service
public interface INacionalidadService extends IServiceGenerico<Nacionalidad, Long>{
    
    Nacionalidad buscarNacionalidadPorNombre(String nombre);
    List<Nacionalidad> listarNacionalidades();
}
