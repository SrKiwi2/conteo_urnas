package com.usic.conteo.controller.ElecionesGenerales;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.usic.conteo.anotaciones.ValidarUsuarioAutenticado;
import com.usic.conteo.config.Encriptar;
import com.usic.conteo.model.IService.IDetalleVotoService;
import com.usic.conteo.model.IService.IMesaGeneralService;
import com.usic.conteo.model.IService.IVotoGeneralService;
import com.usic.conteo.model.dto.ResultadosMesaGuardarDto;
import com.usic.conteo.model.entityGeneral.DetalleVoto;
import com.usic.conteo.model.service.ResultadoVotoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/votog")
public class VotoDetalleController {

    private final ResultadoVotoService resultadoVotoService;
    private final IMesaGeneralService mesaGeneralService;
    private final IVotoGeneralService votoGeneralService;
    private final IDetalleVotoService detalleVotoService;

    @ValidarUsuarioAutenticado
    @GetMapping("/vista")
    public String vista() {
        return "eleccion_general/voto/vista";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/tabla-registro")
    public String tablaRegisdtros(Model model) throws Exception {

        List<DetalleVoto> listarVotosGenerales = detalleVotoService.listarDetalleVotos();
        List<String> encrypteIds = new ArrayList<>();
        for (DetalleVoto DetallesVotos : listarVotosGenerales) {
            String id_encryptado = Encriptar.encrypt(Long.toString(DetallesVotos.getIdDetalleVoto()));
            encrypteIds.add(id_encryptado);
        }

        model.addAttribute("listaVotos", listarVotosGenerales);
        model.addAttribute("id_encryptado", encrypteIds);
        return "eleccion_general/voto/tabla-registro";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model) {
        model.addAttribute("detalleVoto", new DetalleVoto());
        model.addAttribute("listaMesas", mesaGeneralService.listarMesaGeneral()); // mesas para el select
        model.addAttribute("listaVotosGenerales", votoGeneralService.listarVotoGeneral()); // votoGeneral para el select
        return "eleccion_general/voto/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/formulario-edit/{idDetalleVoto}")
    public String formularioEdit(Model model, @PathVariable("idDetalleVoto") String idDetalleVoto) throws Exception {

        Long id = Long.parseLong(Encriptar.decrypt(idDetalleVoto));
        DetalleVoto dv = detalleVotoService.findById(id);
        model.addAttribute("detalleVoto", dv);
        model.addAttribute("edit", true);
        model.addAttribute("listaMesas", mesaGeneralService.listarMesaGeneral());
        model.addAttribute("listaVotosGenerales", votoGeneralService.listarVotoGeneral());
        return "eleccion_general/voto/formulario";
    }

    @ValidarUsuarioAutenticado
    @PostMapping("/guardar-resultados-mesa")
    public ResponseEntity<?> guardarResultadosMesa(
            @RequestParam("idMesa") Long idMesa,
            @RequestParam("pdc") Integer pdc,
            @RequestParam("libre") Integer libre,
            @RequestParam("nulo") Integer nulo,
            @RequestParam("blanco") Integer blanco) {

        var dto = new ResultadosMesaGuardarDto(idMesa, pdc, libre, nulo, blanco);
        resultadoVotoService.guardarResultadosMesa(dto);
        return ResponseEntity.ok().build();
    }

    // @ValidarUsuarioAutenticado
    // @PostMapping("/registrar-voto")
    // @Transactional
    // public ResponseEntity<String> registrar(HttpServletRequest request,
    // @Validated @ModelAttribute DetalleVoto voto) {

    // String numero = voto.getNumeroMesa() != null ? voto.getNumeroMesa().trim() :
    // "";
    // Long idRecinto = (voto.getRecinto() != null) ?
    // voto.getRecinto().getIdRecinto() : null;

    // if (idRecinto == null || numero.isEmpty()) {
    // return ResponseEntity.badRequest().body("Datos incompletos.");
    // }

    // if (mesaGeneralService.existsByNumeroMesaAndRecinto(numero, idRecinto)) {
    // return ResponseEntity.ok("Ya existe una mesa con ese número en el recinto
    // seleccionado");
    // }

    // Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
    // voto.setNumeroMesa(numero);
    // voto.setRegistroIdUsuario(usuario.getIdUsuario());
    // voto.setEstado("ACTIVO");
    // Recinto rec = recintoService.findById(idRecinto);
    // voto.setRecinto(rec);
    // mesaGeneralService.save(voto);

    // return ResponseEntity.ok("Se realizó el registro correctamente");
    // }

    // @PostMapping("/modificar-voto")
    // public ResponseEntity<String> modificar(HttpServletRequest request,
    // DetalleVoto voto, RedirectAttributes redirectAttributes) {

    // Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
    // voto.setRegistroIdUsuario(usuario.getIdUsuario());
    // voto.setEstado("ACTIVO");
    // mesaGeneralService.save(voto);
    // return ResponseEntity.ok("Se modifico correctament");
    // }

    // @ValidarUsuarioAutenticado
    // @PostMapping("/eliminar/{idMesaGeneral}")
    // public ResponseEntity<String> eliminar(@PathVariable("idMesaGeneral") String
    // idMesaGeneral) throws Exception {

    // Long id = Long.parseLong(Encriptar.decrypt(idMesaGeneral));
    // DetalleVoto voto = mesaGeneralService.findById(id);
    // voto.setEstado("ELIMINADO");
    // mesaGeneralService.save(voto);
    // return ResponseEntity.ok("Registro Eliminado");
    // }
}
