package com.usic.conteo.model.service;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.usic.conteo.model.dao.VotoLiveRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LiveDashboardService {
    private final VotoLiveRepository repo;
    private final EntityManager em;

    public Map<String, Object> loadDashboard() {
        var porRecinto = repo.findAllAggRaw();
        var porProvincia = repo.sumByProvincia();
        var topMun = repo.topMunicipios(PageRequest.of(0, 10));

        // KPIs globales
        long totalHabil = 0, totalVotos = 0, recintosConVoto = 0, mesasConVoto = mesasConVotos();
        for (var r : porRecinto) {
            totalHabil += ((Number) r.getOrDefault("habilitados", 0)).longValue();
            int tv = ((Number) r.getOrDefault("total", 0)).intValue();
            totalVotos += tv;
            if (tv > 0)
                recintosConVoto++;
        }

        Map<String, Object> kpis = Map.of(
                "total", totalVotos,
                "habilitados", totalHabil,
                "participacionPct", totalHabil > 0 ? Math.round(10000.0 * totalVotos / totalHabil) / 100.0 : 0.0,
                "recintosConVoto", recintosConVoto,
                "mesasConVoto", mesasConVoto);

        return Map.of(
                "kpis", kpis,
                "porProvincia", porProvincia,
                "topMunicipios", topMun,
                "porRecinto", porRecinto,
                "updatedAt", java.time.LocalDateTime.now().toString());
    }

    private long mesasConVotos() {
        // número de mesas con al menos un detalle
        var q = em.createNativeQuery("""
                  SELECT COUNT(DISTINCT dv.id_mesa_general)
                  FROM detalle_voto dv
                """);
        return ((Number) q.getSingleResult()).longValue();
    }
}
