package com.usic.conteo.controller.mesa;
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
import com.usic.conteo.model.IService.IJuradoService;
import com.usic.conteo.model.IService.IMesaService;
import com.usic.conteo.model.entity.Mesa;
import com.usic.conteo.model.entity.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mesa")
@RequiredArgsConstructor
public class MesaController {
    
    private final IMesaService iMesaService;

    private final IJuradoService iJuradoService;

    private final ICarreraService carreraService;

    @ValidarUsuarioAutenticado
    @GetMapping("/vista")
    public String inicio() {
        return "mesa/vista";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tabla-registros")
    public String tablaRegistros(Model model) throws Exception {

        //List<Mesa> listaMesas = iMesaService.listarMesas();
        List<Mesa> listaMesas = iMesaService.listarMesas();
        for (Mesa mesa : listaMesas) {
            for (Object[] resultado : iMesaService.findMesasWithRestantes(mesa.getId_mesa())) {
                mesa.setRestante((Long) resultado[0]);
                mesa.setRegistrado((Long) resultado[1]);
            }
        }
        List<String> encryptedIds = new ArrayList<>();
        for (Mesa mesas : listaMesas) {
            String id_encryptado = Encriptar.encrypt(Long.toString(mesas.getId_mesa()));
            encryptedIds.add(id_encryptado);
        }
        model.addAttribute("listaMesas", listaMesas);
        model.addAttribute("id_encryptado", encryptedIds);
        //model.addAttribute("listaMesas",iMesaService.findMesasWithRestantes());

        return "mesa/tabla-registro";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model, Mesa mesa) {
        model.addAttribute("listaJurados", iJuradoService.listarJurados());
        model.addAttribute("listarCarreras", carreraService.listarCarreras());
        return "mesa/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario-edit/{id_mesa}")
    public String formularioEdit(Model model, @PathVariable("id_mesa") String id_mesa) throws Exception {

        Long id = Long.parseLong(Encriptar.decrypt(id_mesa));
        model.addAttribute("listaJurados", iJuradoService.listarJurados());
        model.addAttribute("listarCarreras", carreraService.listarCarreras());
        model.addAttribute("mesa", iMesaService.findById(id));
        model.addAttribute("edit", "true");

        return "mesa/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/registrar-mesa")
    public ResponseEntity<String> registrar(HttpServletRequest request, @Validated Mesa mesa) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        mesa.setModificacionIdUsuario(usuario.getIdUsuario());
        mesa.setEstado("ACTIVO");
        iMesaService.save(mesa);

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @PostMapping(value = "/modificar-mesa")
    public ResponseEntity<String> modificar(HttpServletRequest request, Mesa mesa,
            RedirectAttributes redirectAttrs) {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        mesa.setModificacionIdUsuario(usuario.getIdUsuario());
        mesa.setEstado("ACTIVO");
        iMesaService.save(mesa);

        return ResponseEntity.ok("Se realizó la modificación correctamente");
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id_mesa}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id_mesa") String id_mesa) throws Exception {

        Long id = Long.parseLong(Encriptar.decrypt(id_mesa));
        Mesa mesa = iMesaService.findById(id);
        mesa.setEstado("ELIMINADO");
        iMesaService.save(mesa);

        return ResponseEntity.ok("Registro Eliminado");
    }
}
