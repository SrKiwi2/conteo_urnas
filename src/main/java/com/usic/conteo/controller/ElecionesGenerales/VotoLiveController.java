package com.usic.conteo.controller.ElecionesGenerales;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usic.conteo.model.service.LiveDashboardService;
import com.usic.conteo.model.service.ResumenVotoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/votol")
public class VotoLiveController {
    private final ResumenVotoService resumenVotoService;
    private final LiveDashboardService service;

    @ResponseBody
    @GetMapping(value = "/api/resumen", produces="application/json")
    public ResponseEntity<Map<String,Object>> resumen() {
        return ResponseEntity.ok(resumenVotoService.obtenerResumen());
    }

    // Vista del dashboard
    @GetMapping("/live")
    public String live() {
        return "eleccion_general/live"; // la vista de abajo
    }

    @ResponseBody
    @GetMapping(value = "/api/dashboard", produces="application/json")
    public Map<String,Object> dashboard() {
        return service.loadDashboard();
    }
}
