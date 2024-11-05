package com.usic.conteo.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.conteo.model.entity.Jurado;

public interface IJuradoDao extends JpaRepository<Jurado, Long> {
    
}
