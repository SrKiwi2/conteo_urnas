package com.usic.conteo.model.IService;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.entity.Mesa;

@Service
public interface IMesaService extends IServiceGenerico<Mesa, Long>{

    List<Mesa> listarMesas();

    List<Object[]> findMesasWithRestantes(Long id_mesa);
    List<Mesa> listarMesasPorIdCarrera(Long idCarrera);
}
