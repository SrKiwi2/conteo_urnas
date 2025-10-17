package com.usic.conteo.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entityGeneral.Departamento;

@Service
public interface IDepartamentoService extends IServiceGenerico<Departamento, Long> {
    List<Departamento> listarDepartamentos();
}
