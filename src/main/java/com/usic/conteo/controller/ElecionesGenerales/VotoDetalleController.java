package com.usic.conteo.controller.ElecionesGenerales;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.usic.conteo.anotaciones.ValidarUsuarioAutenticado;
import com.usic.conteo.componet.SseBroadcaster;
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
    private final SseBroadcaster sse;

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
        model.addAttribute("listaMesas", mesaGeneralService.listarMesasSinResultados()); // mesas para el select
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

    @GetMapping("/stream")
    public SseEmitter stream() {
        return sse.subscribe();
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

        // Notifica a todos los clientes: mesa cerrada y tabla cambió
        Map<String, Object> payload = Map.of(
            "idMesa", idMesa,
            "tipo", "MESA_REGISTRADA",
            "ts", System.currentTimeMillis()
        );
        sse.sendEvent("mesa-registrada", payload);
        
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mesag/pendientes")
    public ResponseEntity<List<MesaGeneralDto>> mesasPendientes() {
        var data = mesaGeneralService.listarMesasSinResultados().stream()
            .map(m -> new MesaGeneralDto(
                m.getIdMesaGeneral(),
                m.getNumeroMesa(),
                m.getRecinto().getNombre(),
                m.getRecinto().getMunicipio().getNombre()
            ))
            .toList();
        return ResponseEntity.ok(data);
    }

    public record MesaGeneralDto(Long id, String numeroMesa, String recintoNombre, String municipioNombre) {}
}
