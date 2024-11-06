package com.usic.conteo.model.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IFacultadService;
import com.usic.conteo.model.dao.IFacutadDao;
import com.usic.conteo.model.entity.Facultad;

@Service
public class FacultadServiceImpl implements IFacultadService{

    @Autowired
    private IFacutadDao facutadDao;

    @Override
    public List<Facultad> findAll() {
        return  facutadDao.findAll();
    }

    @Override
    public Facultad findById(Long idEntidad) {
        return facutadDao.findById(idEntidad).orElse(null);
    }

    @Override
    public Facultad save(Facultad entidad) {
        return facutadDao.save(entidad);
    }    

    @Override
    public void deleteById(Long idEntidad) {
        facutadDao.deleteById(idEntidad);
    }

    @Override
    public Facultad buscarFacultadPorNombre(String nombre_facultad) {
        return facutadDao.buscarFacultadPorNombre(nombre_facultad);
    }

    @Override
    public List<Facultad> listarFacultades() {
        return facutadDao.listarFacultades();
    }
    
}
