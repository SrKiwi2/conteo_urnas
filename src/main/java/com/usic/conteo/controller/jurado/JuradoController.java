package com.usic.conteo.controller.jurado;

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
import com.usic.conteo.model.IService.IJuradoService;
import com.usic.conteo.model.IService.IPersonaService;
import com.usic.conteo.model.entity.Jurado;
import com.usic.conteo.model.entity.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/jurado")
@RequiredArgsConstructor
public class JuradoController {
    
    private final IPersonaService personaService;

    private final IJuradoService juradoService;

    @ValidarUsuarioAutenticado
    @GetMapping("/vista")
    public String inicio() {
        return "jurado/vista";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tabla-registros")
    public String tablaRegistros(Model model) throws Exception {
        List<Jurado> listaJurados = juradoService.listarJurados();
        List<String> encryptedIds = new ArrayList<>();
        for (Jurado jurados : listaJurados) {
            String id_encryptado = Encriptar.encrypt(Long.toString(jurados.getId_jurado()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("listaJurados", listaJurados);
        model.addAttribute("id_encryptado", encryptedIds);
        return "jurado/tabla-registro";
    }
    
    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model, Jurado jurado) {
        model.addAttribute("listaPersonas", personaService.listarPersonas());
        return "jurado/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario-edit/{id_jurado}")
    public String formularioEdit(Model model, @PathVariable("id_jurado") String idJurado) throws Exception {
        Long id = Long.parseLong(Encriptar.decrypt(idJurado));
        model.addAttribute("jurado", juradoService.findById(id));
        model.addAttribute("listaPersonas", personaService.listarPersonas());
        model.addAttribute("edit", "true");
        return "jurado/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/registrar-jurado")
    public ResponseEntity<String> registrar(HttpServletRequest request, @Validated Jurado jurado) {
        if (juradoService.findById(jurado.getPersona().getIdPersona()) == null) {
            Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");
            jurado.setRegistroIdUsuario(usuarioLogueado.getIdUsuario());
            jurado.setEstado("ACTIVO");
            juradoService.save(jurado);
            return ResponseEntity.ok("Se realizó el registro correctamente");
        } else {
            return ResponseEntity.ok("Ya existe el usuario y contraseña");
        }
    }

    @PostMapping(value = "/modificar-jurado")
    public ResponseEntity<String> modificar(HttpServletRequest request, Jurado jurado,
            RedirectAttributes redirectAttrs) {
        Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");
        jurado.setModificacionIdUsuario(usuarioLogueado.getIdUsuario());
        jurado.setEstado("ACTIVO");
        juradoService.save(jurado);
        return ResponseEntity.ok("Se realizó la modificación correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id_jurado}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id_jurado") String idJurado) throws Exception {
        Long id = Long.parseLong(Encriptar.decrypt(idJurado));
        Jurado jurado = juradoService.findById(id);
        jurado.setEstado("ELIMINADO");
        juradoService.save(jurado);
        return ResponseEntity.ok("Registro Eliminado");
    }
}
