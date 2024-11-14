package com.usic.conteo.controller.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.usic.conteo.anotaciones.ValidarUsuarioAutenticado;
import com.usic.conteo.config.Encriptar;
import com.usic.conteo.model.IService.ICarreraService;
import com.usic.conteo.model.IService.IFacultadService;
import com.usic.conteo.model.IService.IVotoService;
import com.usic.conteo.model.Repository.ConsultasVistaVotos;
import com.usic.conteo.model.entity.Carrera;
import com.usic.conteo.model.entity.Facultad;
import com.usic.conteo.model.entity.Voto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/publico")
@RequiredArgsConstructor
public class VistaPublicaController {

    private final IVotoService votoService;
    private final IFacultadService facultadService;
    private final ICarreraService carreraService;

    private final ConsultasVistaVotos consultasVistaVotos;

    
    @GetMapping(value = "/vista")
    public String vistaPublica(HttpServletRequest request,@Validated Voto voto, Model model) {
        model.addAttribute("votos", votoService.findAll());

        List<Carrera> listarCarrera = carreraService.listarCarreras();
        model.addAttribute("listarCarreras", listarCarrera);
        return "publico/vista";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tabla-votos")
    public String tablaVotos(Model model, HttpServletRequest request) throws Exception {

        List<Facultad> listarFacultad = facultadService.listarFacultades();
        
        List<String> encryptedIds = new ArrayList<>();
        for (Facultad facultades : listarFacultad) {
            String id_encryptado = Encriptar.encrypt(Long.toString(facultades.getId_facultad()));
            encryptedIds.add(id_encryptado);
        }

        model.addAttribute("listarFacultades", listarFacultad);
       
        model.addAttribute("id_encryptado", encryptedIds);

        return "voto/tabla-registro";
    }


    @PostMapping("/formularioFacultad/{id_facultad}")
    public String formularioFacultad(HttpServletRequest request, Model model,
            @PathVariable("id_facultad") Long id_facultad) {
        
        String tipo_mesa = "ESTUDIANTE";
        List<Map<String, Object>> resultado = consultasVistaVotos.obtenerIdsDeGrupo(id_facultad, tipo_mesa);
        
        System.out.println(resultado);
        System.out.println(id_facultad);

        Long sumValido = null;
        Long sumBlanco = null;
        Long sumNulo = null;
        
        // Iterar sobre los resultados y asignar los valores a las variables
        for (Map<String, Object> row : resultado) {
            String tipoVoto = (String) row.get("tipo_voto");
            Long sum = (Long) row.get("sum");
            
            if ("VALIDO".equals(tipoVoto)) {
                sumValido = sum;
            } else if ("BLANCO".equals(tipoVoto)) {
                sumBlanco = sum;
            } else if ("NULO".equals(tipoVoto)) {
                sumNulo = sum;
            }
        }
        
        // Verificar los valores obtenidos
        System.out.println("Sum Válido: " + sumValido);
        System.out.println("Sum Blanco: " + sumBlanco);
        System.out.println("Sum Nulo: " + sumNulo);
        
        // Puedes guardar estos valores en el request para usarlos en el frontend
        request.setAttribute("sumValido", sumValido);
        request.setAttribute("sumBlanco", sumBlanco);
        request.setAttribute("sumNulo", sumNulo);
        
        model.addAttribute("sumValido", sumValido);
        model.addAttribute("sumBlanco", sumBlanco);
        model.addAttribute("sumNulo", sumNulo);
        return "redirect:/publico/vista";
    }
}
