package com.usic.conteo.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.conteo.model.entity.Facultad;

public interface IFacutadDao extends JpaRepository<Facultad, Long>{
    
}
