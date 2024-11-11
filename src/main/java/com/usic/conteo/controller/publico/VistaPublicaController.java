package com.usic.conteo.controller.publico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.usic.conteo.anotaciones.ValidarUsuarioAutenticado;
import com.usic.conteo.config.Encriptar;
import com.usic.conteo.model.IService.IMesaService;
import com.usic.conteo.model.IService.IVotoService;
import com.usic.conteo.model.Repository.ConsultasVistaVotos;
import com.usic.conteo.model.entity.Mesa;
import com.usic.conteo.model.entity.Usuario;
import com.usic.conteo.model.entity.Voto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class VistaPublicaController {

    private final IVotoService votoService;
    private final IMesaService iMesaService;
    private final ConsultasVistaVotos consultasVistaVotos;

    // @GetMapping("/")
    // public String getMethodName() {
    //     return "redirect:/vista_publica";
    // }
    
    @GetMapping(value = "/publica")
    public String vistaPublica(HttpServletRequest request,@Validated Voto voto, Model model) {
        model.addAttribute("votos", votoService.findAll());

        Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");

        Mesa mesa = iMesaService.findById(consultasVistaVotos.ObtenerMesaXUsuario(usuarioLogueado.getIdUsuario()));

        model.addAttribute("conteoNulos", consultasVistaVotos.ObtenerConteoXMesa("NULO", mesa.getId_mesa()));
        model.addAttribute("conteoBlancos", consultasVistaVotos.ObtenerConteoXMesa("BLANCO", mesa.getId_mesa()));
        model.addAttribute("conteoValidos", consultasVistaVotos.ObtenerConteoXMesa("VALIDO", mesa.getId_mesa()));
        Integer totalNulos = consultasVistaVotos.ObtenerConteoXMesa("NULO", mesa.getId_mesa());
        Integer totalBlancos = consultasVistaVotos.ObtenerConteoXMesa("BLANCO", mesa.getId_mesa());
        Integer totalValidos = consultasVistaVotos.ObtenerConteoXMesa("VALIDO", mesa.getId_mesa());
        Integer totalVotos = totalNulos + totalBlancos + totalValidos;
        model.addAttribute("totalVotos", totalVotos);

        model.addAttribute("conteoNulos", totalNulos);
        model.addAttribute("conteoBlancos", totalBlancos);
        model.addAttribute("conteoValidos", totalValidos);
        model.addAttribute("totalVotos", totalVotos);
        return "publico/vista";
    }

}
