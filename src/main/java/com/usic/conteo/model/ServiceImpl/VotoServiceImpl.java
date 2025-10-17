package com.usic.conteo.model.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IVotoService;
import com.usic.conteo.model.dao.IVotoDao;
import com.usic.conteo.model.entity.Voto;

@Service
public class VotoServiceImpl implements IVotoService{

    @Autowired
    private IVotoDao iVotoDao;

    public List<Voto> findAll() {
        // TODO Auto-generated method stub
        return iVotoDao.findAll();
    }

    @Override
    public Voto findById(Long idEntidad) {
        // TODO Auto-generated method stub
        return iVotoDao.findById(idEntidad).orElse(null);
    }

    @Override
    public Voto save(Voto entidad) {
        // TODO Auto-generated method stub
        return iVotoDao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        // TODO Auto-generated method stub
        iVotoDao.deleteById(idEntidad);
    }

    @Override
    public List<Voto> listarVotos() {
        // TODO Auto-generated method stub
        return iVotoDao.listarVotos();
    }

}
