package com.usic.conteo.controller.ElecionesGenerales;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usic.conteo.anotaciones.ValidarUsuarioAutenticado;
import com.usic.conteo.model.service.ResumenVotoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/votol")
public class VotoLiveController {
    private final ResumenVotoService resumenVotoService;

    @ResponseBody
    @GetMapping("/api/resumen")
    public ResponseEntity<Map<String,Object>> resumen() {
        return ResponseEntity.ok(resumenVotoService.obtenerResumen());
    }

    // Vista del dashboard
    @ValidarUsuarioAutenticado
    @GetMapping("/live")
    public String live() {
        return "eleccion_general/live"; // la vista de abajo
    }
}
