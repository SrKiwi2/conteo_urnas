package com.usic.conteo.model.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IUsuarioService;
import com.usic.conteo.model.dao.IUsuarioDao;
import com.usic.conteo.model.entity.Usuario;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService{

    private final IUsuarioDao usuarioDao;

    private final PasswordEncoder encoder;

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

    @Override
    public Usuario autenticar(String username, String rawPassword) {
        var u = usuarioDao.buscarUsuarioPorNombre(username);
        if (u == null) return null;

        // Soporte migración: si coincide texto plano, re-hash y guarda
        String stored = u.getPassword();
        if (stored != null && !stored.startsWith("$2")) {
        if (stored.equals(rawPassword)) {
            u.setPassword(encoder.encode(rawPassword));
            usuarioDao.save(u);
            return u;
        }
        return null;
        }
        // Normal: verificar BCrypt
        return encoder.matches(rawPassword, stored) ? u : null;
    }
}
