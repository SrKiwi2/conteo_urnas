package com.usic.conteo.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entity.Docente;

@Service
public interface IDocenteService extends IServiceGenerico<Docente, Long>{
    Docente buscarDocentePorRD(String rd);
    List<Docente> listarDocentes();
}
