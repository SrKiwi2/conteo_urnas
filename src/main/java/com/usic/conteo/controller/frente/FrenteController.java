package com.usic.conteo.controller.frente;
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
import com.usic.conteo.model.IService.IFrenteService;
import com.usic.conteo.model.IService.IMesaService;
import com.usic.conteo.model.entity.Frente;
import com.usic.conteo.model.entity.Persona;
import com.usic.conteo.model.entity.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/frente")
@RequiredArgsConstructor
public class FrenteController {
    private IFrenteService frenteService;

    @ValidarUsuarioAutenticado
    @GetMapping("/vista")
    public String inicio() {
        return "frente/vista";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tabla-registros")
    public String tablaRegistros(Model model) throws Exception {

        List<Frente> listaFrentes = frenteService.findAll();
        List<String> encryptedIds = new ArrayList<>();
        for (Frente frentes : listaFrentes) {
            String id_encryptado = Encriptar.encrypt(Long.toString(frentes.getId_frente()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("listaFrentes", listaFrentes);
        model.addAttribute("id_encryptado", encryptedIds);

        return "frente/tabla-registro";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model, Frente frente) {
        return "frente/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario-edit/{id_frente}")
    public String formularioEdit(Model model, @PathVariable("id_frente") String id_frente) throws Exception {

        Long id = Long.parseLong(Encriptar.decrypt(id_frente));
        model.addAttribute("frente", frenteService.findById(id));
        model.addAttribute("edit", "true");

        return "frente/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/registrar-frente")
    public ResponseEntity<String> registrar(HttpServletRequest request, @Validated Frente frente) {

        frente.setEstado("ACTIVO");
        frente.setNombre_frente(frente.getNombre_frente());
        frenteService.save(frente);

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @PostMapping(value = "/modificar-frente")
    public ResponseEntity<String> modificar(HttpServletRequest request, Frente frente,
            RedirectAttributes redirectAttrs) {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        frente.setModificacionIdUsuario(usuario.getIdUsuario());
        frente.setEstado("ACTIVO");
        frenteService.save(frente);

        return ResponseEntity.ok("Se realizó la modificación correctamente");

    }

    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id_frente}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id_frente") String id_frente) throws Exception {

        Long id = Long.parseLong(Encriptar.decrypt(id_frente));
        Frente frente = frenteService.findById(id);
        frente.setEstado("ELIMINADO");
        frenteService.save(frente);

        return ResponseEntity.ok("Registro Eliminado");
    }
}
