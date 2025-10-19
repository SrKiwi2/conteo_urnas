package com.usic.conteo.model.service;

import java.util.HashMap;
import java.util.List;
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
        //var porRecinto = repo.findRecintoAgg();
        var porProvincia = repo.sumByProvincia();
        var topMun = repo.topMunicipios(PageRequest.of(0, 10));

        var porRecintoAgg = repo.findRecintoAgg();

        List<Map<String,Object>> porRecinto = porRecintoAgg.stream().map(r -> {
            Map<String,Object> m = new HashMap<>();
            m.put("recinto",     r.getRecinto());
            m.put("municipio",   r.getMunicipio());
            m.put("provincia",   r.getProvincia());
            m.put("pdc",         safeLong(r.getPdc()));
            m.put("libre",       safeLong(r.getLibre()));
            m.put("nulo",        safeLong(r.getNulo()));
            m.put("blanco",      safeLong(r.getBlanco()));
            m.put("total",       safeLong(r.getTotal()));
            m.put("habilitados", safeLong(r.getHabilitados()));
            return m;
        }).toList();

        // KPIs globales
        long totalHabil = 0, totalVotos = 0, recintosConVoto = 0, mesasConVoto = mesasConVotos();
        for (var r : porRecinto) {
            totalHabil += num(r.get("habilitados"));
            long tv = num(r.get("total"));
            totalVotos += tv;
            if (tv > 0) recintosConVoto++;
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

        private static long num(Object o) { return o == null ? 0L : ((Number)o).longValue(); }

    private static long safeLong(Long v) { return v == null ? 0L : v; }
}
