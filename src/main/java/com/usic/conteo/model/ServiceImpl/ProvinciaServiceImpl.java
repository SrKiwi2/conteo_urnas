package com.usic.conteo.model.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IProvinciaService;
import com.usic.conteo.model.dao.IProvinciaDao;
import com.usic.conteo.model.entityGeneral.Provincia;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProvinciaServiceImpl implements IProvinciaService {
    private final IProvinciaDao dao;

    @Override
    public List<Provincia> findAll() {
        return dao.findAll();
    }

    @Override
    public Provincia findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public Provincia save(Provincia entidad) {
        return dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        dao.deleteById(idEntidad);
    }

    @Override
    public List<Provincia> listarProvincias() {
        return dao.listarProvincias();
    }
}
