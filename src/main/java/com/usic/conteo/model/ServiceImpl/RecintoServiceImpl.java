package com.usic.conteo.model.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IRecintoService;
import com.usic.conteo.model.dao.IRecintoDao;
import com.usic.conteo.model.entityGeneral.Municipio;
import com.usic.conteo.model.entityGeneral.Recinto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecintoServiceImpl implements IRecintoService {
    
    private final IRecintoDao dao;

    @Override
    public List<Recinto> findAll() {
        return dao.findAll();
    }

    @Override
    public Recinto findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public Recinto save(Recinto entidad) {
        return dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        dao.deleteById(idEntidad);
    }

    @Override
    public List<Recinto> listarRecintos() {
        return dao.listarRecintos();
    }

    @Override
    public Recinto buscarNombre(String nombre) {
        return dao.buscarNombre(nombre);
    }

    @Override
    public Optional<Recinto> findByNombreAndMunicipio(String nombre, Municipio municipio) {
        return dao.findByNombreAndMunicipio(nombre, municipio);
    }

    @Override
    public Optional<Recinto> findByNombreAndMunicipio_IdMunicipio(String nombre, Long municipioId) {
        return dao.findByNombreAndMunicipio_IdMunicipio(nombre, municipioId);
    }
}