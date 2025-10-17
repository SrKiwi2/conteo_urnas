package com.usic.conteo.controller.ElecionesGenerales;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usic.conteo.anotaciones.ValidarUsuarioAutenticado;
import com.usic.conteo.config.Encriptar;
import com.usic.conteo.model.IService.IMesaGeneralService;
import com.usic.conteo.model.IService.IRecintoService;
import com.usic.conteo.model.entity.Usuario;
import com.usic.conteo.model.entityGeneral.MesaGeneral;
import com.usic.conteo.model.entityGeneral.Recinto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mesag")
public class MesaGeneralController {
    
    private final IRecintoService recintoService;
    private final IMesaGeneralService mesaGeneralService;

    @ValidarUsuarioAutenticado
    @GetMapping("/vista")
    public String vista() {
        return "eleccion_general/mesaGeneral/vista";
    }
    
    @ValidarUsuarioAutenticado
    @PostMapping("/tabla-registro")
    public String tablaRegisdtros(Model model) throws Exception {
        
        List<MesaGeneral> listarMesas = mesaGeneralService.listarMesaGeneral();
        List<String> encrypteIds = new ArrayList<>();
        for (MesaGeneral mesas : listarMesas) {
            String id_encryptado = Encriptar.encrypt(Long.toString(mesas.getIdMesaGeneral()));
            encrypteIds.add(id_encryptado);
        }
        
        model.addAttribute("listarMesas", listarMesas);
        model.addAttribute("id_encryptado", encrypteIds);
        return "eleccion_general/mesaGeneral/tabla-registro";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model) {
        List<Recinto> listaRecintos = recintoService.listarRecintos();
        model.addAttribute("mesaGeneral",new MesaGeneral());
        model.addAttribute("listaRecintos", listaRecintos);
        return "eleccion_general/mesaGeneral/formulario";
    }
    
    @ValidarUsuarioAutenticado
    @PostMapping("/formulario-edit/{idMesaGeneral}")
    public String formularioEdit(Model model, @PathVariable("idMesaGeneral") String idMesaGeneral) throws Exception {
        
        Long id = Long.parseLong(Encriptar.decrypt(idMesaGeneral));
        model.addAttribute("mesaGeneral", mesaGeneralService.findById(id));
        model.addAttribute("edit", "true");
        model.addAttribute("listaRecintos", recintoService.listarRecintos());
        return "eleccion_general/mesaGeneral/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/registrar-mesaGeneral")
    @Transactional
    public ResponseEntity<String> registrar(HttpServletRequest request, @Validated @ModelAttribute MesaGeneral mesaGeneral) {

        String numero = mesaGeneral.getNumeroMesa() != null ? mesaGeneral.getNumeroMesa().trim() : "";
        Long idRecinto = (mesaGeneral.getRecinto() != null) ? mesaGeneral.getRecinto().getIdRecinto() : null;

        if (idRecinto == null || numero.isEmpty()) {
            return ResponseEntity.badRequest().body("Datos incompletos.");
        }

        if (mesaGeneralService.existsByNumeroMesaAndRecinto(numero, idRecinto)) {
            return ResponseEntity.ok("Ya existe una mesa con ese número en el recinto seleccionado");
        }

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        mesaGeneral.setNumeroMesa(numero);
        mesaGeneral.setRegistroIdUsuario(usuario.getIdUsuario());
        mesaGeneral.setEstado("ACTIVO");
        Recinto rec = recintoService.findById(idRecinto);
        mesaGeneral.setRecinto(rec);
        mesaGeneralService.save(mesaGeneral); 
        
        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @PostMapping("/modificar-mesaGeneral")
    public ResponseEntity<String> modificar(HttpServletRequest request, MesaGeneral mesaGeneral, RedirectAttributes redirectAttributes) {
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        mesaGeneral.setRegistroIdUsuario(usuario.getIdUsuario());
        mesaGeneral.setEstado("ACTIVO");
        mesaGeneralService.save(mesaGeneral); 
        return ResponseEntity.ok("Se modifico correctament");
    }
    
    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{idMesaGeneral}")
    public ResponseEntity<String> eliminar(@PathVariable("idMesaGeneral") String idMesaGeneral) throws Exception {
        
        Long id = Long.parseLong(Encriptar.decrypt(idMesaGeneral));
        MesaGeneral mesaGeneral = mesaGeneralService.findById(id);
        mesaGeneral.setEstado("ELIMINADO");
        mesaGeneralService.save(mesaGeneral);
        return ResponseEntity.ok("Registro Eliminado");
    }
}
