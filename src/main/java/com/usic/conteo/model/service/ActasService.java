package com.usic.conteo.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.dao.VotoLiveRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActasService {
    
      private final VotoLiveRepository repo;

  public List<Map<String,Object>> listarActasPorMunicipio() {
    return repo.actasPorMunicipio();
  }

  public Map<String,Object> actasDeMunicipio(Long idMunicipio) {
    return repo.actasPorMunicipio(idMunicipio);
  }
}
