package com.usic.conteo.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.dao.VotoLiveRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MunicipioVotosService {
    
      private final VotoLiveRepository repo;

  public List<Map<String,Object>> listar() { return repo.votosPorMunicipio(); }
  
  public Map<String,Object> uno(Long id) { return repo.votosPorMunicipio(id); }
}
