package com.usic.conteo.model.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultasVistaVotos {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long ObtenerMesaXUsuario(long v_id_usuario) {
        String sql = "select m.id_mesa from mesa m inner join jurado j on j.id_jurado = m.id_jurado inner join usuario u on u.id_jurado = j.id_jurado where u.id_usuario = ? and u._estado = 'ACTIVO' and j._estado = 'ACTIVO' and m._estado = 'ACTIVO'";
        Object[] params = new Object[] {v_id_usuario};
    
        try {
            return jdbcTemplate.queryForObject(sql, params, Long.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Integer ObtenerConteoXMesa(String tipo_voto,long v_id_mesa) {
        String sql = "select count(voto.id_voto) from voto inner join mesa on mesa.id_mesa = voto.id_mesa where voto.tipo_voto = ? and mesa.id_mesa = ? and voto._estado = 'ACTIVO' and mesa._estado = 'ACTIVO'";
        Object[] params = new Object[] {tipo_voto,v_id_mesa};
    
        try {
            return jdbcTemplate.queryForObject(sql, params, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Map<String, Object>> votosTipoMEsaFacultad(Long id_facultad, String tipo_mesa) {
        String sql = "SELECT v.tipo_voto, SUM(CAST(v.cantidad AS INT)) " + 
                     "FROM voto v " + 
                     "INNER JOIN mesa m ON m.id_mesa = v.id_mesa " + 
                     "INNER JOIN carrera c ON c.id_carrera = m.id_carrera " + 
                     "INNER JOIN facultad f ON f.id_facultad = c.id_facultad " + 
                     "WHERE f.id_facultad = ? AND m.tipo_mesa = ? " + 
                     "AND m._estado = 'ACTIVO' " + 
                     "AND f._estado = 'ACTIVO' " + 
                     "AND c._estado = 'ACTIVO' " + 
                     "AND v._estado = 'ACTIVO' " + 
                     "GROUP BY v.tipo_voto " + 
                     "ORDER BY v.tipo_voto ASC";
        
        Object[] params = new Object[] {id_facultad, tipo_mesa};
    
        try {
            return jdbcTemplate.queryForList(sql, params);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Map<String, Object>> votosPorCarrera(Long id_carrera, String tipo_mesa) {
        String sql = "SELECT v.tipo_voto, SUM(CAST(v.cantidad AS INT)) " + 
                     "FROM voto v " + 
                     "INNER JOIN mesa m ON m.id_mesa = v.id_mesa " + 
                     "INNER JOIN carrera c ON c.id_carrera = m.id_carrera " + 
                     "INNER JOIN facultad f ON f.id_facultad = c.id_facultad " + 
                     "WHERE c.id_carrera = ? AND m.tipo_mesa = ? " + 
                     "AND m._estado = 'ACTIVO' " + 
                     "AND f._estado = 'ACTIVO' " + 
                     "AND c._estado = 'ACTIVO' " + 
                     "AND v._estado = 'ACTIVO' " + 
                     "GROUP BY v.tipo_voto " + 
                     "ORDER BY v.tipo_voto ASC";
        
        Object[] params = new Object[] {id_carrera, tipo_mesa};
    
        try {
            return jdbcTemplate.queryForList(sql, params);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    

    public List<Map<String, Object>> listarVotosTipoMesa(String tipo_mesa) {
        String sql = "SELECT v.tipo_voto, SUM(CAST(v.cantidad AS INT)) " +
                     "FROM voto v " +
                     "INNER JOIN mesa m ON m.id_mesa = v.id_mesa " +
                     "INNER JOIN carrera c ON c.id_carrera = m.id_carrera " +
                     "INNER JOIN facultad f ON f.id_facultad = c.id_facultad " +
                     "WHERE m.tipo_mesa = ? " +
                     "AND m._estado = 'ACTIVO' " +
                     "AND f._estado = 'ACTIVO' " +
                     "AND c._estado = 'ACTIVO' " +
                     "AND v._estado = 'ACTIVO' " +
                     "GROUP BY v.tipo_voto " +
                     "ORDER BY v.tipo_voto ASC";
    
        Object[] params = new Object[] {tipo_mesa};
    
        try {
            return jdbcTemplate.queryForList(sql, params);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Map<String, Object>> listarVotosTotal() {
        String sql = "SELECT v.tipo_voto, SUM(CAST(v.cantidad AS INT)) " +
                     "FROM voto v " +
                     "INNER JOIN mesa m ON m.id_mesa = v.id_mesa " +
                     "INNER JOIN carrera c ON c.id_carrera = m.id_carrera " +
                     "INNER JOIN facultad f ON f.id_facultad = c.id_facultad " +
                     "WHERE m._estado = 'ACTIVO' " +
                     "AND f._estado = 'ACTIVO' " +
                     "AND c._estado = 'ACTIVO' " +
                     "AND v._estado = 'ACTIVO' " +
                     "GROUP BY v.tipo_voto " +
                     "ORDER BY v.tipo_voto ASC";
    
        Object[] params = new Object[] {};  // Si no hay parámetros dinámicos, no es necesario incluirlos
    
        try {
            return jdbcTemplate.queryForList(sql, params);
        } catch (EmptyResultDataAccessException e) {
            return null;  // Retorna null si no se encuentran resultados
        }
    }
    
    public List<Map<String, Object>> ObtenerConteoDeVotosXTipoMesaXFacultad(Long id_facultad, String tipo_mesa) {
        String sql = "SELECT SUM(CAST(v.cantidad AS INT)) FROM voto v INNER JOIN mesa m ON m.id_mesa = v.id_mesa inner join carrera c on c.id_carrera = m.id_carrera inner join facultad f on f.id_facultad = c.id_facultad where f.id_facultad = ? and m.tipo_mesa = ? and m._estado = 'ACTIVO' and f._estado = 'ACTIVO' and c._estado = 'ACTIVO' and v._estado = 'ACTIVO' group by v.tipo_voto order by v.tipo_voto asc;";
        Object[] params = new Object[] {id_facultad, tipo_mesa};
    
        try {
            return jdbcTemplate.queryForList(sql, params);
        } catch (EmptyResultDataAccessException e) {
            
            return null;
        }
    }

    public List<Map<String, Object>> ObtenerConteoDeVotosXTipoMesa(Long id_facultad, String v_tipo_mesa) {
        String sql = "SELECT SUM(CAST(v.cantidad AS INT)) FROM voto v INNER JOIN mesa m ON m.id_mesa = v.id_mesa inner join carrera c on c.id_carrera = m.id_carrera inner join facultad f on f.id_facultad = c.id_facultad where m.tipo_mesa = ? and m.'_estado' = 'ACTIVO and f.'_estado' = 'ACTIVO' and c.'_estado' = 'ACTIVO' and v.'_estado' = 'ACTIVO' group by v.tipo_voto order by v.tipo_voto asc;";
        Object[] params = new Object[] {id_facultad, v_tipo_mesa};
    
        try {
            return jdbcTemplate.queryForList(sql, params);
        } catch (EmptyResultDataAccessException e) {
            
            return null;
        }
    }

    public List<Map<String, Object>> listarVotosEstudiantesRenovacion(String tipoMesa, String nombreFrente) {
        String sql = "SELECT v.tipo_voto, SUM(CAST(v.cantidad AS INT)) AS total_votos " +
                     "FROM voto v " +
                     "INNER JOIN mesa m ON m.id_mesa = v.id_mesa " +
                     "INNER JOIN frente f ON f.id_frente = v.id_frente " +
                     "WHERE m.tipo_mesa = ? " +
                     "AND f.nombre_frente = ? " +
                     "AND m._estado = 'ACTIVO' " +
                     "AND v._estado = 'ACTIVO' " +
                     "GROUP BY v.tipo_voto " +
                     "ORDER BY v.tipo_voto ASC";
    
        try {
            return jdbcTemplate.queryForList(sql, tipoMesa, nombreFrente);
        } catch (EmptyResultDataAccessException e) {
            return null; // mejor que null
        }
    }
    
    
}
