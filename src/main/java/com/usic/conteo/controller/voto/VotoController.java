package com.usic.conteo.controller.voto;
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
import com.usic.conteo.model.IService.IRolService;
import com.usic.conteo.model.IService.IVotoService;
import com.usic.conteo.model.Repository.ConsultasVistaVotos;
import com.usic.conteo.model.entity.Frente;
import com.usic.conteo.model.entity.Rol;
import com.usic.conteo.model.entity.Usuario;
import com.usic.conteo.model.entity.Voto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/voto")
@RequiredArgsConstructor
public class VotoController {
    
    private final IVotoService iVotoService;

    private final ConsultasVistaVotos consultasVistaVotos;

    @ValidarUsuarioAutenticado
    @GetMapping("/vista")
    public String inicio() {
        return "voto/vista";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tabla-registros")
    public String tablaRegistros(Model model) throws Exception {
        List<Voto> listaVotos = iVotoService.listarVotos();
        List<String> encryptedIds = new ArrayList<>();
        for (Voto votos : listaVotos) {
            String id_encryptado = Encriptar.encrypt(Long.toString(votos.getId_voto()));
            encryptedIds.add(id_encryptado);
        }

        model.addAttribute("listaVotos", listaVotos);
        model.addAttribute("id_encryptado", encryptedIds);

        return "voto/tabla-registro";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model, Voto voto) {
        return "voto/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario-edit/{id_voto}")
    public String formularioEdit(Model model, @PathVariable("id_voto") String id_voto) throws Exception{
        Long id = Long.parseLong(Encriptar.decrypt(id_voto));
        model.addAttribute("voto", iVotoService.findById(id));
        model.addAttribute("edit", "true");
        return "voto/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/registrar-voto")
    public ResponseEntity<String> registrar(HttpServletRequest request, @Validated Voto voto) {

        System.out.println("================================================================");
        System.out.println(consultasVistaVotos.ObtenerMesaXUsuario(1L));
        System.out.println("================================================================");

        voto.setEstado("ACTIVO");
        iVotoService.save(voto);

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @PostMapping(value = "/modificar-voto")
    public ResponseEntity<String> modificar(HttpServletRequest request, Voto voto, RedirectAttributes redirectAttrs) {
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        voto.setModificacionIdUsuario(usuario.getIdUsuario());
        voto.setEstado("ACTIVO");
        iVotoService.save(voto);
        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id_voto}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id_voto") String id_voto) throws Exception {
        Long id = Long.parseLong(Encriptar.decrypt(id_voto));
        Voto voto = iVotoService.findById(id);
        voto.setEstado("ELIMINADO");
        iVotoService.save(voto);
        return ResponseEntity.ok("Registro Eliminado");
    }
}
