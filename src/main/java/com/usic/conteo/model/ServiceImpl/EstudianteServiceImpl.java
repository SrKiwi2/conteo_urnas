package com.usic.conteo.model.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IEstudianteService;
import com.usic.conteo.model.dao.IEstudianteDao;
import com.usic.conteo.model.entity.Estudiante;

@Service
public class EstudianteServiceImpl implements IEstudianteService {
    
    @Autowired
    private IEstudianteDao dao;

    @Override
    public List<Estudiante> findAll() {
        return dao.findAll();
    }

    @Override
    public Estudiante findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public Estudiante save(Estudiante entidad) {
        return dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        dao.deleteById(idEntidad);
    }

    @Override
    public Estudiante buscarEstudinatePorRU(String ru) {
        return dao.buscarEstudinatePorRU(ru);
    }

    @Override
    public List<Estudiante> listarEstudiantes() {
        return dao.listarEstudiantes();
    }
}
