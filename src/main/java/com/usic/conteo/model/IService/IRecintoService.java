package com.usic.conteo.model.IService;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entityGeneral.Municipio;
import com.usic.conteo.model.entityGeneral.Recinto;

@Service
public interface IRecintoService extends IServiceGenerico<Recinto, Long> {
    List<Recinto> listarRecintos();
    Recinto buscarNombre(String nombre);
    Optional<Recinto> findByNombreAndMunicipio(String nombre, Municipio municipio);
    Optional<Recinto> findByNombreAndMunicipio_IdMunicipio(String nombre, Long municipioId);
}
