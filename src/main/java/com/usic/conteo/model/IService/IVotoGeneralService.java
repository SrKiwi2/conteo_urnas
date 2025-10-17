package com.usic.conteo.model.IService;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entityGeneral.VotoGeneral;

@Service
public interface IVotoGeneralService extends IServiceGenerico<VotoGeneral, Long>{
    List<VotoGeneral> listarVotoGeneral();
    Optional<VotoGeneral> findByVotoIgnoreCase(String voto);
}
