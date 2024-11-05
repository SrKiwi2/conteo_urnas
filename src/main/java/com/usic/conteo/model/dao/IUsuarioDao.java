package com.usic.conteo.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usic.conteo.model.entity.Usuario;

public interface IUsuarioDao extends JpaRepository <Usuario, Long>{

    @Query(value = "select * from usuario u where u._estado='ACTIVO' and u.nombre = ?1 and u.password = ?2", nativeQuery = true)
    Usuario UsuarioyContraseña(String usuario, String password);

    @Query("SELECT u FROM Usuario u WHERE u.nombre = ?1 AND u.estado = 'ACTIVO'")
    Usuario buscarUsuarioPorNombre(String nombre);

    @Query("SELECT u FROM Usuario u WHERE u.estado = 'ACTIVO'")
    List<Usuario> listarUsuarios();
    
}
