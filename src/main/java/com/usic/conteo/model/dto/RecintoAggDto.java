package com.usic.conteo.model.dto;

public record RecintoAggDto(
        Long idRecinto, String recinto,
        Long idMunicipio, String municipio,
        Long idProvincia, String provincia,
        Integer pdc, Integer libre, Integer nulo, Integer blanco,
        Integer total, Integer habilitados, Double participacionPct) {
}
