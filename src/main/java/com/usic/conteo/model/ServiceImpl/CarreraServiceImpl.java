package com.usic.conteo.model.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.ICarreraService;
import com.usic.conteo.model.dao.ICarreraDao;
import com.usic.conteo.model.entity.Carrera;

@Service
public class CarreraServiceImpl implements ICarreraService{

    @Autowired
    private ICarreraDao carreraDao;

    @Override
    public List<Carrera> findAll() {
        return carreraDao.findAll();
    }

    @Override
    public Carrera findById(Long idEntidad) {
        return carreraDao.findById(idEntidad).orElse(null);
    }

    @Override
    public Carrera save(Carrera entidad) {
        return carreraDao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        carreraDao.deleteById(idEntidad);
    }

    @Override
    public Carrera buscarCarreraPorNombre(String nombre_carrera) {
        return carreraDao.buscarCarreraPorNombre(nombre_carrera);
    }

    @Override
    public List<Carrera> listarCarreras() {
        return carreraDao.listarCarreras();
    }
    
}
