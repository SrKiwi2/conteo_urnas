package com.usic.conteo.model.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.INacionalidadService;
import com.usic.conteo.model.dao.INacionadalidadDao;
import com.usic.conteo.model.entity.Nacionalidad;

@Service
public class NacionalidadServiceImpl implements INacionalidadService{
    
    @Autowired
    private INacionadalidadDao dao;

    @Override
    public List<Nacionalidad> findAll() {
        return dao.findAll();
    }

    @Override
    public Nacionalidad findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public Nacionalidad save(Nacionalidad entidad) {
        return dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        dao.deleteById(idEntidad);
    }

    @Override
    public Nacionalidad buscarNacionalidadPorNombre(String nombre) {
        return dao.buscarNacionalidadPorNombre(nombre);
    }

    @Override
    public List<Nacionalidad> listarNacionalidades() {
        return dao.listarNacionalidades();
    }
}
