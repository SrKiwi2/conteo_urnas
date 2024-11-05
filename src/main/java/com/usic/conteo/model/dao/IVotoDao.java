package com.usic.conteo.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.conteo.model.entity.Voto;

public interface IVotoDao extends JpaRepository <Voto, Long>{
    
}
