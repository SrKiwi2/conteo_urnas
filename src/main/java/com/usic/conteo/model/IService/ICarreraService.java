package com.usic.conteo.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entity.Carrera;

@Service
public interface ICarreraService extends IServiceGenerico<Carrera, Long>{
    Carrera buscarCarreraPorNombre(String nombre_carrera);
    List<Carrera> listarCarreras();
}
