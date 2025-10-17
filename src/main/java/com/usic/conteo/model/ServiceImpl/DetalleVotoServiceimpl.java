package com.usic.conteo.model.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IDetalleVotoService;
import com.usic.conteo.model.dao.IDetalleVotoDao;
import com.usic.conteo.model.entityGeneral.DetalleVoto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetalleVotoServiceimpl implements IDetalleVotoService{
    
    private final IDetalleVotoDao dao;

    @Override
    public List<DetalleVoto> findAll() {
        return dao.findAll();
    }

    @Override
    public DetalleVoto findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public DetalleVoto save(DetalleVoto entidad) {
        return dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        dao.deleteById(idEntidad);
    }

    @Override
    public List<DetalleVoto> listarDetalleVotos() {
        return dao.listarDetalleVotos();
    }

    @Override
    public Optional<DetalleVoto> findByMesaGeneralIdMesaGeneralAndVotoGeneralIdVotoGeneral(Long idMesa, Long idVoto) {
        return dao.findByMesaGeneralIdMesaGeneralAndVotoGeneralIdVotoGeneral(idMesa, idVoto);
    }

    @Override
    public List<DetalleVoto> listarPorMesa(Long idMesa) {
        return dao.listarPorMesa(idMesa);
    }
}
