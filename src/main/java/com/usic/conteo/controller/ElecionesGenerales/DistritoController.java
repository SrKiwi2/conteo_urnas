package com.usic.conteo.controller.ElecionesGenerales;

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
import com.usic.conteo.model.IService.IDistritoService;
import com.usic.conteo.model.entity.Usuario;
import com.usic.conteo.model.entityGeneral.Municipio;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/distrito")
public class DistritoController {

    private final IDistritoService distritoService;
    
    @ValidarUsuarioAutenticado
    @GetMapping("/vista")
    public String vista() {
        return "eleccion_general/distrito/vista";
    }
    
    @ValidarUsuarioAutenticado
    @PostMapping("/tabla-registro")
    public String tablaRegisdtros(Model model) throws Exception {
        
        List<Municipio> listaDistritos = distritoService.listarDistrito();
        List<String> encrypteIds = new ArrayList<>();
        for (Municipio distritos : listaDistritos) {
            String id_encryptado = Encriptar.encrypt(Long.toString(distritos.getIdMunicipio()));
            encrypteIds.add(id_encryptado);
        }
        model.addAttribute("listaDistritos", listaDistritos);
        model.addAttribute("id_encryptado", encrypteIds);
        return "eleccion_general/distrito/tabla-registro";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Municipio distrito) {
        return "eleccion_general/distrito/formulario";
    }
    
    @ValidarUsuarioAutenticado
    @PostMapping("/formulario-edit/{idDistrito}")
    public String formularioEdit(Model model, @PathVariable("idDistrito") String idDistrito) throws Exception {
        
        Long id = Long.parseLong(Encriptar.decrypt(idDistrito));
        model.addAttribute("distrito", distritoService.findById(id));
        model.addAttribute("edit", "true");
        return "eleccion_general/distrito/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/registrar-distrito")
    public ResponseEntity<String> registrarFacultad(HttpServletRequest request, @Validated Municipio distrito) {

        if (distritoService.buscarNombre(distrito.getNombre()) == null) {
           Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
           distrito.setRegistroIdUsuario(usuario.getIdUsuario());
           distrito.setEstado("ACTIVO");
           distritoService.save(distrito); 
           return ResponseEntity.ok("Se realizó el registro correctamente");
        }else{
           return ResponseEntity.ok("Ya existe un registro con este nombre");
        }
    }

    @PostMapping("/modificar-distrito")
    public ResponseEntity<String> modificar(HttpServletRequest request, Municipio distrito, RedirectAttributes redirectAttributes) {
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        distrito.setRegistroIdUsuario(usuario.getIdUsuario());
        distrito.setEstado("ACTIVO");
        distritoService.save(distrito); 
        return ResponseEntity.ok("Se modifico correctament");
    }
    
    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{idDistrito}")
    public ResponseEntity<String> eliminar(@PathVariable("idDistrito") String idDistrito) throws Exception {
        
        Long id = Long.parseLong(Encriptar.decrypt(idDistrito));
        Municipio distrito = distritoService.findById(id);
        distrito.setEstado("ELIMINADO");
        distritoService.save(distrito);
        return ResponseEntity.ok("Registro Eliminado");
    }
}
