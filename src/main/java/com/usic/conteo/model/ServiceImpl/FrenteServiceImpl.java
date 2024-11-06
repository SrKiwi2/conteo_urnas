package com.usic.conteo.model.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IFrenteService;
import com.usic.conteo.model.dao.IFrenteDao;
import com.usic.conteo.model.entity.Frente;

@Service
public class FrenteServiceImpl implements IFrenteService{

    @Autowired
    private IFrenteDao iFrenteDao;

    @Override
    public List<Frente> findAll() {
        // TODO Auto-generated method stub
        return iFrenteDao.findAll();
    }

    @Override
    public Frente findById(Long idEntidad) {
        // TODO Auto-generated method stub
        return iFrenteDao.findById(idEntidad).orElse(null);
    }

    @Override
    public Frente save(Frente entidad) {
        // TODO Auto-generated method stub
        return iFrenteDao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        // TODO Auto-generated method stub
        iFrenteDao.deleteById(idEntidad);
    }

    @Override
    public List<Frente> listarFrentes() {
        // TODO Auto-generated method stub
        return iFrenteDao.listarFrentes();
    }
    
}
