package com.usic.conteo.model.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IJuradoService;
import com.usic.conteo.model.dao.IJuradoDao;
import com.usic.conteo.model.entity.Jurado;

@Service
public class JuradoServiceImpl implements IJuradoService{
    
    @Autowired
    private IJuradoDao dao;

    @Override
    public List<Jurado> findAll() {
        return dao.findAll();
    }

    @Override
    public Jurado findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public Jurado save(Jurado entidad) {
        return dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        dao.deleteById(idEntidad);
    }

    @Override
    public List<Jurado> listarJurados() {
        return dao.listarJurados();
    }
}
