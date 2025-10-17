package com.usic.conteo.model.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IVotoGeneralService;
import com.usic.conteo.model.dao.IVotoGeneralDao;
import com.usic.conteo.model.entityGeneral.VotoGeneral;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VotoGeneralServiceImpl implements IVotoGeneralService {
    
    private final IVotoGeneralDao dao;

    @Override
    public List<VotoGeneral> findAll() {
        return dao.findAll();
    }

    @Override
    public VotoGeneral findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public VotoGeneral save(VotoGeneral entidad) {
        return dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        dao.deleteById(idEntidad);
    }

    @Override
    public List<VotoGeneral> listarVotoGeneral() {
        return dao.listarVotoGeneral();
    }

    @Override
    public Optional<VotoGeneral> findByVotoIgnoreCase(String voto) {
        return dao.findByVotoIgnoreCase(voto);
    }
}
