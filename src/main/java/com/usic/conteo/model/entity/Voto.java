package com.usic.conteo.model.entity;
import java.util.HashSet;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "voto")
@Setter
@Getter
public class Voto extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_voto;
    private String tipo_voto;
    private String estado_voto;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mesa")
    private Mesa mesa;

    @ManyToMany(mappedBy = "votos")
    private Set<Voto> votos = new HashSet<>();


    // @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    // @JoinTable(
    //     name = "grupo_persona",
    //     joinColumns = @JoinColumn(name = "id_grupo"),
    //     inverseJoinColumns = @JoinColumn(name = "id_persona")
    // )
    // private Set<Persona> personas = new HashSet<>();

    // @ManyToMany(mappedBy = "personas")
    // private Set<Grupo> grupos = new HashSet<>();
}

