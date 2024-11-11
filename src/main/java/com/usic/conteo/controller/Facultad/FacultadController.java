package com.usic.conteo.controller.Facultad;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usic.conteo.anotaciones.ValidarUsuarioAutenticado;
import com.usic.conteo.config.Encriptar;
import com.usic.conteo.model.IService.IFacultadService;
import com.usic.conteo.model.entity.Facultad;
import com.usic.conteo.model.entity.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/facultad")
@RequiredArgsConstructor
public class FacultadController {

    private final IFacultadService facultadService;

    @ValidarUsuarioAutenticado
    @GetMapping("/vista")
    public String inicio() {
        return "facultad/vista";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tabla-registro")
    public String tablaRegisdtros(Model model) throws Exception {
        
        List<Facultad> listaFacultades = facultadService.listarFacultades();
        List<String> encrypteIds = new ArrayList<>();
        for (Facultad facultades : listaFacultades) {
            String id_encryptado = Encriptar.encrypt(Long.toString(facultades.getId_facultad()));
            encrypteIds.add(id_encryptado);
        }
        model.addAttribute("listaFacultades", listaFacultades);
        model.addAttribute("id_encryptado", encrypteIds);

        return "facultad/tabla-registro";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Facultad facultad) {
        return "facultad/formulario";
    }
    
    @ValidarUsuarioAutenticado
    @PostMapping("/formulario-edit/{id_facultad}")
    public String formularioEdit(Model model, @PathVariable("id_facultad") String id_facultad) throws Exception {
        
        Long id = Long.parseLong(Encriptar.decrypt(id_facultad));
        model.addAttribute("facultad", facultadService.findById(id));
        model.addAttribute("edit", "true");
        
        return "facultad/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/registrar-facultad")
    public ResponseEntity<String> registrarFacultad(HttpServletRequest request, @Validated Facultad facultad) {

        if (facultadService.buscarFacultad(facultad.getNombre_facultad()) == null) {
           Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
           facultad.setRegistroIdUsuario(usuario.getIdUsuario());
           facultad.setEstado("ACTIVO");
           facultadService.save(facultad); 

           return ResponseEntity.ok("Se realizó el registro correctamente");
        }else{
           return ResponseEntity.ok("Ya existe un registro con este nombre");
        }
    
    }

    @PostMapping("/modificar-facultad")
    public ResponseEntity<String> modificar(HttpServletRequest request, Facultad facultad, RedirectAttributes redirectAttributes) {
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        facultad.setRegistroIdUsuario(usuario.getIdUsuario());
        facultad.setEstado("ACTIVO");
        facultadService.save(facultad); 
        
        return ResponseEntity.ok("Se modifico correctament");
    }
    
    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id_facultad}")
    public ResponseEntity<String> eliminar(@PathVariable("id_facultad") String id_facultad) throws Exception {
        
        Long id = Long.parseLong(Encriptar.decrypt(id_facultad));
        Facultad facultad = facultadService.findById(id);
        facultad.setEstado("ELIMINADO");
        facultadService.save(facultad);
        
        return ResponseEntity.ok("Registro Eliminado");
    }
    
    
    
    
}
