package com.usic.conteo.controller.voto;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usic.conteo.anotaciones.ValidarUsuarioAutenticado;
import com.usic.conteo.config.Encriptar;
import com.usic.conteo.model.IService.IFrenteService;
import com.usic.conteo.model.IService.IMesaService;
import com.usic.conteo.model.IService.IVotoService;
import com.usic.conteo.model.entity.Mesa;
import com.usic.conteo.model.entity.Usuario;
import com.usic.conteo.model.entity.Voto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/voto")
@RequiredArgsConstructor
public class VotoController {
    
    private final IVotoService iVotoService;

    private final IFrenteService iFrenteService;

    private final IMesaService imesaService;

    private final SimpMessagingTemplate messagingTemplate;

    @ValidarUsuarioAutenticado
    @GetMapping("/vista")
    public String inicio() {
        return "voto/vista";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tabla-registros")
    public String tablaRegistros(Model model, HttpServletRequest request) throws Exception {
        List<Voto> listarvotosGeneral = iVotoService.listarVotos();
        List<String> encryptedIds = new ArrayList<>();
        for (Voto votos : listarvotosGeneral) {
            String id_encryptado = Encriptar.encrypt(Long.toString(votos.getId_voto()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("listaVotos", listarvotosGeneral);
        model.addAttribute("id_encryptado", encryptedIds);
        return "voto/tabla-registro";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model, Voto voto, HttpServletRequest request) {
        model.addAttribute("listaFrentes", iFrenteService.listarFrentes());
        model.addAttribute("listarMesa", imesaService.listarMesas());
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

    // @ValidarUsuarioAutenticado
    // @PostMapping("/registrar-voto")
    // public ResponseEntity<String> registrar(HttpServletRequest request, @Validated Voto voto, 
    //     @RequestParam(value = "tipoVoto", required = false) Integer tipoVoto, 
    //     @RequestParam(value = "cantidad_valido") Integer cantidad_valido,
    //     @RequestParam(value = "cantidad_blanco") Integer cantidad_blanco,
    //     @RequestParam(value = "cantidad_nulo") Integer cantidad_null) {

    //     Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");

    //     if (cantidad_valido > 0) {
    //         voto.setTipo_voto("VALIDO");
    //         voto.setEstado("ACTIVO");
    //         voto.setMesa(voto.getMesa());
    //         voto.setRegistroIdUsuario(usuarioLogueado.getIdUsuario());
    //         voto.setCantidad(Integer.toString(cantidad_valido));
    //         iVotoService.save(voto);
    //     }

    //     if (cantidad_valido > 0) {
    //         Voto voto2 = new Voto();

    //         voto2.setTipo_voto("NULO");
    //         voto2.setEstado("ACTIVO");
    //         voto2.setMesa(voto.getMesa());
    //         voto2.setFrente(null);
    //         voto2.setRegistroIdUsuario(usuarioLogueado.getIdUsuario());
    //         voto2.setCantidad(Integer.toString(cantidad_null));
    //         iVotoService.save(voto2);
    //     }

    //     if (cantidad_valido > 0) {
    //         Voto voto3 = new Voto();
    //         voto3.setTipo_voto("BLANCO");
    //         voto3.setFrente(null);
    //         voto3.setEstado("ACTIVO");
    //         voto3.setMesa(voto.getMesa());
    //         voto3.setRegistroIdUsuario(usuarioLogueado.getIdUsuario());
    //         voto3.setCantidad(Integer.toString(cantidad_blanco));
    //         iVotoService.save(voto3);
    //     }

    //     messagingTemplate.convertAndSend("/topic/actualizar-graficos", "actualizar");
    //     return ResponseEntity.ok("Se realizó el registro correctamente");
    // }

    @ValidarUsuarioAutenticado
    @PostMapping("/registrar-voto")
    public ResponseEntity<String> registrarVotoMultiple(HttpServletRequest request,
            @RequestParam Long id_mesa,
            @RequestParam Long frente1,
            @RequestParam Integer cantidad_frente1,
            @RequestParam Long frente2,
            @RequestParam Integer cantidad_frente2,
            @RequestParam Integer cantidad_blanco,
            @RequestParam Integer cantidad_nulo) {

        Mesa mesa = imesaService.findById(id_mesa);
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        // Voto Frente 1
        Voto voto1 = new Voto();
        voto1.setTipo_voto("VALIDO");
        voto1.setCantidad(cantidad_frente1.toString());
        voto1.setMesa(mesa);
        voto1.setFrente(iFrenteService.findById(frente1));
        voto1.setRegistroIdUsuario(usuario.getIdUsuario());
        voto1.setEstado("ACTIVO");
        iVotoService.save(voto1);

        // Voto Frente 2
        Voto voto2 = new Voto();
        voto2.setTipo_voto("VALIDO");
        voto2.setCantidad(cantidad_frente2.toString());
        voto2.setMesa(mesa);
        voto2.setFrente(iFrenteService.findById(frente2));
        voto2.setRegistroIdUsuario(usuario.getIdUsuario());
        voto2.setEstado("ACTIVO");
        iVotoService.save(voto2);

        // Voto Blanco
        Voto votoBlanco = new Voto();
        votoBlanco.setTipo_voto("BLANCO");
        votoBlanco.setCantidad(cantidad_blanco.toString());
        votoBlanco.setMesa(mesa);
        votoBlanco.setFrente(null);
        votoBlanco.setRegistroIdUsuario(usuario.getIdUsuario());
        votoBlanco.setEstado("ACTIVO");
        iVotoService.save(votoBlanco);

        // Voto Nulo
        Voto votoNulo = new Voto();
        votoNulo.setTipo_voto("NULO");
        votoNulo.setCantidad(cantidad_nulo.toString());
        votoNulo.setMesa(mesa);
        votoNulo.setFrente(null);
        votoNulo.setRegistroIdUsuario(usuario.getIdUsuario());
        votoNulo.setEstado("ACTIVO");
        iVotoService.save(votoNulo);

        messagingTemplate.convertAndSend("/topic/actualizar-graficos", "actualizar");

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    /* cuando se vota para un solo frente */
    @ValidarUsuarioAutenticado
    @PostMapping("/registrar-voto-unico")
    public ResponseEntity<String> registrarVotoUnFrente(HttpServletRequest request,
            @RequestParam Long id_mesa,
            @RequestParam Long frenteId,
            @RequestParam Integer cantidad_frente,
            @RequestParam Integer cantidad_blanco,
            @RequestParam Integer cantidad_nulo) {

        Mesa mesa = imesaService.findById(id_mesa);
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        // Voto Frente Único
        Voto votoFrente = new Voto();
        votoFrente.setTipo_voto("VALIDO");
        votoFrente.setCantidad(cantidad_frente.toString());
        votoFrente.setMesa(mesa);
        votoFrente.setFrente(iFrenteService.findById(frenteId));
        votoFrente.setRegistroIdUsuario(usuario.getIdUsuario());
        votoFrente.setEstado("ACTIVO");
        iVotoService.save(votoFrente);

        // Voto Blanco
        Voto votoBlanco = new Voto();
        votoBlanco.setTipo_voto("BLANCO");
        votoBlanco.setCantidad(cantidad_blanco.toString());
        votoBlanco.setMesa(mesa);
        votoBlanco.setFrente(null); // Sin frente
        votoBlanco.setRegistroIdUsuario(usuario.getIdUsuario());
        votoBlanco.setEstado("ACTIVO");
        iVotoService.save(votoBlanco);

        // Voto Nulo
        Voto votoNulo = new Voto();
        votoNulo.setTipo_voto("NULO");
        votoNulo.setCantidad(cantidad_nulo.toString());
        votoNulo.setMesa(mesa);
        votoNulo.setFrente(null); // Sin frente
        votoNulo.setRegistroIdUsuario(usuario.getIdUsuario());
        votoNulo.setEstado("ACTIVO");
        iVotoService.save(votoNulo);

        // Notificar actualización de gráficos
        messagingTemplate.convertAndSend("/topic/actualizar-graficos", "actualizar");

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
