package com.usic.conteo.model.entityGeneral;

import com.usic.conteo.config.AuditoriaConfig;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "departamento")
@Setter @Getter
public class Departamento extends AuditoriaConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDepartamento;

    private String nombre;
}
