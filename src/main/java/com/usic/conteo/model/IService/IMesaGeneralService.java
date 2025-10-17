package com.usic.conteo.model.IService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entityGeneral.MesaGeneral;

@Service
public interface IMesaGeneralService extends IServiceGenerico<MesaGeneral, Long>{
    List<MesaGeneral> listarMesaGeneral();
    boolean existsByNumeroMesaAndRecinto(String numeroMesa, Long idRecinto);
    boolean existsByNumeroMesaAndRecintoExcludingId(String numeroMesa, Long idRecinto, Long idMesaGeneral);
    List<MesaGeneral> listarMesasSinResultados();
}
