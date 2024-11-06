package com.usic.conteo.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entity.Frente;

@Service
public interface IFrenteService extends IServiceGenerico<Frente, Long>{
    
    List<Frente> listarFrentes();
}
