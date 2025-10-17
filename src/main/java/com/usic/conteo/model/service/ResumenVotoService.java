package com.usic.conteo.model.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usic.conteo.model.dao.IDetalleVotoDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumenVotoService {
    
    private final IDetalleVotoDao detalleVotoRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerResumen() {
        int pdc = 0, libre = 0, nulo = 0, blanco = 0;

        for (Object[] row : detalleVotoRepository.sumarPorTipo()) {
            String tipo = (String) row[0];
            Number suma = (Number) row[1];
            int v = (suma == null ? 0 : suma.intValue());
            switch (tipo) {
                case "PDC"    -> pdc = v;
                case "LIBRE"  -> libre = v;
                case "NULO"   -> nulo = v;
                case "BLANCO" -> blanco = v;
                default -> {} // ignora extras
            }
        }
        int total = pdc + libre + nulo + blanco;

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("pdc", pdc);
        out.put("libre", libre);
        out.put("nulo", nulo);
        out.put("blanco", blanco);
        out.put("total", total);
        out.put("updatedAt", java.time.OffsetDateTime.now().toString());
        return out;
    }
}
