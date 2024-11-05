package com.usic.conteo.model.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.ISexoService;
import com.usic.conteo.model.dao.ISexoDao;
import com.usic.conteo.model.entity.Sexo;

@Service
public class SexoServiceImpl implements ISexoService{
    @Autowired
    private ISexoDao dao;

    @Override
    public List<Sexo> findAll() {
        return dao.findAll();
    }

    @Override
    public Sexo findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public Sexo save(Sexo entidad) {
        return dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        dao.deleteById(idEntidad);
    }

    @Override
    public Sexo buscarSexoPorNombre(String nombre) {
        return dao.buscarSexoPorNombre(nombre);
    }

    @Override
    public List<Sexo> listarGeneros() {
        return dao.listarGeneros();
    }
       
}
