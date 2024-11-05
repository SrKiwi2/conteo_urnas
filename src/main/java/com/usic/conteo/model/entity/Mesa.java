package com.usic.conteo.model.entity;
import com.usic.conteo.config.AuditoriaConfig;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mesa")
@Setter
@Getter
public class Mesa extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_mesa;
    private String nombre_mesa;
    private String estado_mesa;
    
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "mesa", fetch = FetchType.LAZY)
    private Voto votos;
}
