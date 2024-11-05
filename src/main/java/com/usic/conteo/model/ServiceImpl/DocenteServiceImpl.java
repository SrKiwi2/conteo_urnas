package com.usic.conteo.model.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IDocenteService;
import com.usic.conteo.model.dao.IDocenteDao;
import com.usic.conteo.model.entity.Docente;

@Service
public class DocenteServiceImpl implements IDocenteService{
    
    @Autowired
    private IDocenteDao dao;

    @Override
    public List<Docente> findAll() {
        return dao.findAll();
    }

    @Override
    public Docente findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public Docente save(Docente entidad) {
        return dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        dao.deleteById(idEntidad);
    }

    @Override
    public Docente buscarDocentePorRD(String rd) {
        return dao.buscarDocentePorRD(rd);
    }

    @Override
    public List<Docente> listarDocentes() {
        return dao.listarDocentes();
    }
}
