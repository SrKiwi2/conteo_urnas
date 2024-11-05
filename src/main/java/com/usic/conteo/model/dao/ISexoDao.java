package com.usic.conteo.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.conteo.model.entity.Sexo;

public interface ISexoDao extends JpaRepository<Sexo, Long>{
    
}
