package com.usic.conteo.model.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IDepartamentoService;
import com.usic.conteo.model.dao.IDepartamentoDao;
import com.usic.conteo.model.entityGeneral.Departamento;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartamentoServiceImpl implements IDepartamentoService {
 
    private final IDepartamentoDao dao;

    @Override
    public List<Departamento> findAll() {
        return dao.findAll();
    }

    @Override
    public Departamento findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public Departamento save(Departamento entidad) {
        return dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        dao.deleteById(idEntidad);
    }

    @Override
    public List<Departamento> listarDepartamentos() {
        return dao.listarDepartamentos();
    }
}
