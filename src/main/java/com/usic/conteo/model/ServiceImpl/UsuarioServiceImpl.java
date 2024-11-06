package com.usic.conteo.model.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IUsuarioService;
import com.usic.conteo.model.dao.IUsuarioDao;
import com.usic.conteo.model.entity.Usuario;

@Service
public class UsuarioServiceImpl implements IUsuarioService{

    @Autowired
    private IUsuarioDao usuarioDao;

    @Override
    public List<Usuario> findAll() {
        return usuarioDao.findAll();
    }

    @Override
    public Usuario findById(Long idEntidad) {
        return usuarioDao.findById(idEntidad).orElse(null);
    }

    @Override
    public Usuario save(Usuario entidad) {
        return usuarioDao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        usuarioDao.deleteById(idEntidad);
    }

    @Override
    public Usuario UsuarioyContraseña(String usuario, String password) {
        return usuarioDao.UsuarioyContraseña(usuario, password);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioDao.listarUsuarios();
    }

    @Override
    public Usuario buscarUsuarioPorNombre(String nombre) {
        return usuarioDao.buscarUsuarioPorNombre(nombre);
    }

    @Override
    public Optional<Usuario> findByPersona_IdPersona(Long idPersona) {
        return usuarioDao.findByPersona_IdPersona(idPersona);
    }


}
