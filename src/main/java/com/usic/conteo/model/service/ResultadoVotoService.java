package com.usic.conteo.model.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.usic.conteo.model.IService.IDetalleVotoService;
import com.usic.conteo.model.IService.IMesaGeneralService;
import com.usic.conteo.model.IService.IVotoGeneralService;
import com.usic.conteo.model.dto.ResultadosMesaGuardarDto;
import com.usic.conteo.model.entityGeneral.DetalleVoto;
import com.usic.conteo.model.entityGeneral.MesaGeneral;
import com.usic.conteo.model.entityGeneral.VotoGeneral;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultadoVotoService {
    private final IMesaGeneralService mesaGeneralService;
    private final IVotoGeneralService votoGeneralRepository;
    private final IDetalleVotoService detalleVotoRepository;

    @Transactional
    public void guardarResultadosMesa(ResultadosMesaGuardarDto dto) {
        // 1) Cargar la mesa (valida existencia)
        MesaGeneral mesa = mesaGeneralService.findById(dto.idMesaGeneral());

        // 2) Resolver/validar tipos de voto
        Map<String, Integer> entradas = Map.of(
            "PDC",   safe(dto.pdc()),
            "LIBRE", safe(dto.libre()),
            "NULO",  safe(dto.nulo()),
            "BLANCO",safe(dto.blanco())
        );

        // (Opcional) Validación de negativos, nulos, etc.
        entradas.forEach((tipo, cantidad) -> {
            if (cantidad < 0) throw new IllegalArgumentException("Cantidad negativa en: " + tipo);
        });

        // 3) Upsert por cada tipo
        for (Map.Entry<String,Integer> e : entradas.entrySet()) {
            String tipo = e.getKey();
            Integer cant = e.getValue();

            VotoGeneral vg = votoGeneralRepository.findByVotoIgnoreCase(tipo)
                .orElseThrow(() -> new IllegalStateException("No existe VotoGeneral: " + tipo));

            // buscar existente por (mesa, tipo)
            DetalleVoto dv = detalleVotoRepository
                .findByMesaGeneralIdMesaGeneralAndVotoGeneralIdVotoGeneral(mesa.getIdMesaGeneral(), vg.getIdVotoGeneral())
                .orElseGet(DetalleVoto::new);

            dv.setMesaGeneral(mesa);
            dv.setVotoGeneral(vg);
            dv.setEstado("ACTIVO");
            dv.setCantidad(cant);

            detalleVotoRepository.save(dv);
        }
    }

    private int safe(Integer v) { return v == null ? 0 : v; }
}
