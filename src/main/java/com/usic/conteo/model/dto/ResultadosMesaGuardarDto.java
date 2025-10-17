package com.usic.conteo.model.dto;


public record ResultadosMesaGuardarDto (
    Long idMesaGeneral,
    Integer pdc,
    Integer libre,
    Integer nulo,
    Integer blanco
){}
