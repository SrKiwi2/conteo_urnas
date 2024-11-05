package com.usic.conteo.model.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IMesaService;
import com.usic.conteo.model.dao.IMesaDao;
import com.usic.conteo.model.entity.Mesa;

@Service
public class MesaServiceImpl implements IMesaService{


    @Autowired
    private IMesaDao iMesaDao;

    @Override
    public List<Mesa> findAll() {
        // TODO Auto-generated method stub
        return iMesaDao.findAll();
    }

    @Override
    public Mesa findById(Long idEntidad) {
        // TODO Auto-generated method stub
        return iMesaDao.findById(idEntidad).orElse(null);
    }

    @Override
    public Mesa save(Mesa entidad) {
        // TODO Auto-generated method stub
        return iMesaDao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        // TODO Auto-generated method stub
        iMesaDao.deleteById(idEntidad);
    }
    
}
