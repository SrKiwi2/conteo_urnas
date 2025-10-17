package com.usic.conteo.model.entityGeneral;

import com.usic.conteo.config.AuditoriaConfig;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "provincia")
@Setter @Getter
public class Provincia extends AuditoriaConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProvincia;

    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento")
    private Departamento departamento;
    
}
