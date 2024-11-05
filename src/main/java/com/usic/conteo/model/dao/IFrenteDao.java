package com.usic.conteo.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.conteo.model.entity.Frente;

public interface IFrenteDao extends JpaRepository <Frente, Long> {
    
}
