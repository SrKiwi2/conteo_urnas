package com.usic.conteo.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.conteo.model.entity.Carrera;

public interface ICarreraDao extends JpaRepository<Carrera, Long>{
    
}
