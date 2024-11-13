package com.usic.conteo.controller.Carrera;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usic.conteo.anotaciones.ValidarUsuarioAutenticado;
import com.usic.conteo.config.Encriptar;
import com.usic.conteo.model.IService.ICarreraService;
import com.usic.conteo.model.IService.IFacultadService;
import com.usic.conteo.model.entity.Carrera;
import com.usic.conteo.model.entity.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/carrera")
@RequiredArgsConstructor
public class CarreraController {

    private final ICarreraService carreraService;
    private final IFacultadService facultadService;

    @ValidarUsuarioAutenticado
    @GetMapping("/vista")
    public String inicio() {
        return "carrera/vista";
    }
    
    @ValidarUsuarioAutenticado
    @PostMapping("/tabla-registro")
    public String tablaRegisdtros(Model model) throws Exception {
        
        List<Carrera> listaCarreras = carreraService.listarCarreras();
        List<String> encrypteIds = new ArrayList<>();
        for (Carrera carreras : listaCarreras) {
            String id_encryptado = Encriptar.encrypt(Long.toString(carreras.getId_carrera()));
            encrypteIds.add(id_encryptado);
        }
        model.addAttribute("listaCarreras", listaCarreras);
        model.addAttribute("id_encryptado", encrypteIds);

        return "carrera/tabla-registro";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model, Carrera carrera) {
        model.addAttribute("listarFacultades", facultadService.listarFacultades());
        return "carrera/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario-edit/{id_carrera}")
    public String formularioEdit(Model model, @PathVariable("id_carrera") String id_carrera) throws Exception {
        
        Long id = Long.parseLong(Encriptar.decrypt(id_carrera));
        model.addAttribute("carrera", carreraService.findById(id));
        model.addAttribute("edit", "true");
        
        return "carrera/formulario";
    }


    @ValidarUsuarioAutenticado
    @PostMapping("/registrar-carrera")
    public ResponseEntity<String> registrarCarrera(HttpServletRequest request, @Validated Carrera carrera) {

        if (carreraService.buscarCarreraPorNombre(carrera.getNombre_carrera()) == null) {
           Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
           carrera.setRegistroIdUsuario(usuario.getIdUsuario());
           carrera.setEstado("ACTIVO");
           carreraService.save(carrera); 

           return ResponseEntity.ok("Se realizó el registro correctamente");
        }else{
           return ResponseEntity.ok("Ya existe un registro con este nombre");
        }
    }

    @PostMapping("/modificar-carrera")
    public ResponseEntity<String> modificar(HttpServletRequest request, Carrera carrera, RedirectAttributes redirectAttributes) {
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        carrera.setRegistroIdUsuario(usuario.getIdUsuario());
        carrera.setEstado("ACTIVO");
        carreraService.save(carrera); 
        
        return ResponseEntity.ok("Se modifico correctament");
    }


    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id_carrera}")
    public ResponseEntity<String> eliminar(@PathVariable("id_carrera") String id_carrera) throws Exception {
        
        Long id = Long.parseLong(Encriptar.decrypt(id_carrera));
        Carrera carrera = carreraService.findById(id);
        carrera.setEstado("ELIMINADO");
        carreraService.save(carrera);
        
        return ResponseEntity.ok("Registro Eliminado");
    }
    
}
