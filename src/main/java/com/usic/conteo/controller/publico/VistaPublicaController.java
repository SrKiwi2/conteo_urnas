package com.usic.conteo.controller.publico;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import com.usic.conteo.model.IService.IVotoService;
import com.usic.conteo.model.entity.Voto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class VistaPublicaController {

    private final IVotoService votoService;

    // @GetMapping("/")
    // public String getMethodName() {
    //     return "redirect:/vista_publica";
    // }
    
    @GetMapping(value = "/publica")
    public String vistaPublica(@Validated Voto voto, Model model) {
        model.addAttribute("votos", votoService.findAll());
        return "publico/vista";
    }
}
