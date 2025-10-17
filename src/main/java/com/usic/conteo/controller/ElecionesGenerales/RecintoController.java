package com.usic.conteo.controller.ElecionesGenerales;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usic.conteo.anotaciones.ValidarUsuarioAutenticado;
import com.usic.conteo.config.Encriptar;
import com.usic.conteo.model.IService.IDistritoService;
import com.usic.conteo.model.IService.IRecintoService;
import com.usic.conteo.model.entity.Usuario;
import com.usic.conteo.model.entityGeneral.Municipio;
import com.usic.conteo.model.entityGeneral.Recinto;
import com.usic.conteo.model.service.RecintoImportService;
import com.usic.conteo.model.service.RecintoImportService.ImportResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recinto")
public class RecintoController {
    
    private final IDistritoService distritoService;
    private final IRecintoService recintoService;
    private final RecintoImportService importService;

    @ValidarUsuarioAutenticado
    @GetMapping("/vista")
    public String vista() {
        return "eleccion_general/recinto/vista";
    }
    
    @ValidarUsuarioAutenticado
    @PostMapping("/tabla-registro")
    public String tablaRegisdtros(Model model) throws Exception {
        
        
        List<Recinto> listarRecintos = recintoService.listarRecintos();
        List<String> encrypteIds = new ArrayList<>();
        for (Recinto distritos : listarRecintos) {
            String id_encryptado = Encriptar.encrypt(Long.toString(distritos.getIdRecinto()));
            encrypteIds.add(id_encryptado);
        }
        
        model.addAttribute("listarRecintos", listarRecintos);
        model.addAttribute("id_encryptado", encrypteIds);
        return "eleccion_general/recinto/tabla-registro";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model) {
        List<Municipio> listaDistritos = distritoService.listarDistrito();
        model.addAttribute("recinto",new Recinto());
        model.addAttribute("listaDistritos", listaDistritos);
        return "eleccion_general/recinto/formulario";
    }
    
    @ValidarUsuarioAutenticado
    @PostMapping("/formulario-edit/{idRecinto}")
    public String formularioEdit(Model model, @PathVariable("idRecinto") String idRecinto) throws Exception {
        
        Long id = Long.parseLong(Encriptar.decrypt(idRecinto));
        model.addAttribute("recinto", recintoService.findById(id));
        model.addAttribute("edit", "true");
        model.addAttribute("listaDistritos", distritoService.listarDistrito());
        return "eleccion_general/recinto/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/registrar-recinto")
    @Transactional
    public ResponseEntity<String> registrar(HttpServletRequest request, @Validated Recinto recinto) {

        if (recintoService.buscarNombre(recinto.getNombre()) == null) {
           Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
           recinto.setRegistroIdUsuario(usuario.getIdUsuario());
           recinto.setEstado("ACTIVO");
           recintoService.save(recinto); 
           return ResponseEntity.ok("Se realizó el registro correctamente");
        }else{
           return ResponseEntity.ok("Ya existe un registro con este nombre");
        }
    }

    @PostMapping("/modificar-recinto")
    public ResponseEntity<String> modificar(HttpServletRequest request, Recinto recinto, RedirectAttributes redirectAttributes) {
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        recinto.setRegistroIdUsuario(usuario.getIdUsuario());
        recinto.setEstado("ACTIVO");
        recintoService.save(recinto); 
        return ResponseEntity.ok("Se modifico correctament");
    }
    
    @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{idRecinto}")
    public ResponseEntity<String> eliminar(@PathVariable("idRecinto") String idRecinto) throws Exception {
        
        Long id = Long.parseLong(Encriptar.decrypt(idRecinto));
        Recinto recinto = recintoService.findById(id);
        recinto.setEstado("ELIMINADO");
        recintoService.save(recinto);
        return ResponseEntity.ok("Registro Eliminado");
    }

    @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<ImportResult> importar(@RequestPart("file") MultipartFile file) throws Exception {
        ImportResult res = importService.importarExcel(file);
        return ResponseEntity.ok(res);
    }
}
