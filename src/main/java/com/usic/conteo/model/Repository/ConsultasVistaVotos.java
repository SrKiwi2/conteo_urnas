package com.usic.conteo.model.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.usic.conteo.model.entity.Voto;

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
}
