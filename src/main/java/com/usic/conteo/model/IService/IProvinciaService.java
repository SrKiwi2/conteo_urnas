package com.usic.conteo.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entityGeneral.Provincia;

@Service
public interface IProvinciaService extends IServiceGenerico<Provincia, Long> {
    List<Provincia> listarProvincias();
}
