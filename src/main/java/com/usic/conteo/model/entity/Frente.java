package com.usic.conteo.model.entity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.usic.conteo.config.AuditoriaConfig;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "frente")
@Setter
@Getter
public class Frente extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_frente;
    private String nombre_frente;
    private String estado_frente;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "frente", fetch = FetchType.LAZY)
	private List<Voto> votos;

}
