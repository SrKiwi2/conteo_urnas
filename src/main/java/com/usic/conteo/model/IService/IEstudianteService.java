package com.usic.conteo.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entity.Estudiante;

@Service
public interface IEstudianteService  extends IServiceGenerico<Estudiante, Long>{
    Estudiante buscarEstudinatePorRU(String ru);
    List<Estudiante> listarEstudiantes();
}
