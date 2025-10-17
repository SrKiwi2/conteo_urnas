package com.usic.conteo.model.IService;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.entity.Usuario;

@Service
public interface IUsuarioService extends IServiceGenerico <Usuario, Long>{
    
    Usuario UsuarioyContraseña(String usuario, String password);

    Usuario buscarUsuarioPorNombre(String nombre);

    List<Usuario> listarUsuarios();

    Optional<Usuario> findByPersona_IdPersona(Long idPersona);

    Usuario autenticar(String username, String rawPassword);
}
