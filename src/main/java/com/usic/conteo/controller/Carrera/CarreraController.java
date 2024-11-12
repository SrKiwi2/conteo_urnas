package com.usic.conteo.controller.Carrera;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.usic.conteo.anotaciones.ValidarUsuarioAutenticado;
import com.usic.conteo.model.IService.ICarreraService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/carrera")
@RequiredArgsConstructor
public class CarreraController {

    private final ICarreraService carreraService;

    @ValidarUsuarioAutenticado
    @GetMapping("/vista")
    public String inicio() {
        return "facultad/vista";
    }
    


    
}
