package com.usic.conteo.model.ServiceImpl;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IDistritoService;
import com.usic.conteo.model.dao.IDistritoDao;
import com.usic.conteo.model.entityGeneral.Municipio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DistritoServiceImpl implements IDistritoService {

    @PersistenceContext
    private EntityManager em;
    
    private final IDistritoDao dao;

    @Override
    public List<Municipio> findAll() {
        return dao.findAll();
    }

    @Override
    public Municipio findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public Municipio save(Municipio entidad) {
        return dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        dao.deleteById(idEntidad);
    }

    @Override
    public List<Municipio> listarDistrito() {
        return dao.listarDistrito();
    }

    @Override
    public Municipio buscarNombre(String nombre) {
        return dao.buscarNombre(nombre);
    }

    @Override
    public List<Municipio> findByIdMunicipioIn(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return em.createQuery(
                "SELECT m FROM Municipio m WHERE m.idMunicipio IN :ids",
                Municipio.class)
            .setParameter("ids", ids)
            .getResultList();
    }
}