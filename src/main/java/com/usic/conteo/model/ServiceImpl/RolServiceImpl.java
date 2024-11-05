package com.usic.conteo.model.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IRolService;
import com.usic.conteo.model.dao.IRolDao;
import com.usic.conteo.model.entity.Rol;

@Service
public class RolServiceImpl implements IRolService{

    @Autowired
    private IRolDao rolDao;

    @Override
    public List<Rol> findAll() {
        return rolDao.findAll();
    }

    @Override
    public Rol findById(Long idEntidad) {
        return rolDao.findById(idEntidad).orElse(null);
    }

    @Override
    public Rol save(Rol entidad) {
        return rolDao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        rolDao.deleteById(idEntidad);
    }

    @Override
    public List<Rol> listarRoles() {
        return rolDao.listarRoles();
    }

    @Override
    public Rol buscarRolPorNombre(String nombre) {
        return rolDao.buscarRolPorNombre(nombre);
    }
    
}
