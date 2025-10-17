package com.usic.conteo.model.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IMesaGeneralService;
import com.usic.conteo.model.dao.IMesaGeneralDao;
import com.usic.conteo.model.entityGeneral.MesaGeneral;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MesaGeneralServiceImpl implements IMesaGeneralService {
    
    private final IMesaGeneralDao dao;

    @Override
    public List<MesaGeneral> findAll() {
        return dao.findAll();
    }

    @Override
    public MesaGeneral findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public MesaGeneral save(MesaGeneral entidad) {
        return dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        dao.deleteById(idEntidad);
    }

    @Override
    public List<MesaGeneral> listarMesaGeneral() {
        return dao.listarMesaGeneral();
    }

    @Override
    public boolean existsByNumeroMesaAndRecinto(String numeroMesa, Long idRecinto) {
        return dao.existsByNumeroMesaIgnoreCaseAndRecinto_IdRecinto(numeroMesa, idRecinto);
    }

    @Override
    public boolean existsByNumeroMesaAndRecintoExcludingId(String numeroMesa, Long idRecinto, Long idMesaGeneral) {
        return dao.existsByNumeroMesaIgnoreCaseAndRecinto_IdRecintoAndIdMesaGeneralNot(numeroMesa, idRecinto, idMesaGeneral);
    }

    @Override
    public List<MesaGeneral> listarMesasSinResultados() {
        return dao.listarMesasSinResultados();
    }
}
