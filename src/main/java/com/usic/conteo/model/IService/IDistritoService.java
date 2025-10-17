package com.usic.conteo.model.IService;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entityGeneral.Municipio;

@Service
public interface IDistritoService extends IServiceGenerico<Municipio, Long> {
    List<Municipio> listarDistrito();
    Municipio buscarNombre(String nombre);
    List<Municipio> findByIdMunicipioIn(Collection<Long> ids);
}
