package com.usic.conteo.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usic.conteo.model.entity.Mesa;

public interface IMesaDao extends JpaRepository <Mesa, Long>{
    
}
