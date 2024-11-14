package com.usic.conteo.controller.publico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usic.conteo.anotaciones.ValidarUsuarioAutenticado;
import com.usic.conteo.config.Encriptar;
import com.usic.conteo.model.IService.ICarreraService;
import com.usic.conteo.model.IService.IFacultadService;
import com.usic.conteo.model.IService.IMesaService;
import com.usic.conteo.model.Repository.ConsultasVistaVotos;
import com.usic.conteo.model.entity.Facultad;
import com.usic.conteo.model.entity.Mesa;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/publico")
@RequiredArgsConstructor
public class VistaPublicaController {

    private final IFacultadService facultadService;
    private final ICarreraService carreraService;
    private final ConsultasVistaVotos consultasVistaVotos;
    private final IMesaService iMesaService;

    @GetMapping(value = "/vista")
    public String vistaPublica(Model model) {
        List<Facultad> listarFacultad = facultadService.listarFacultades();
        model.addAttribute("listarFacultades", listarFacultad);

        String tipo_mesa = "ESTUDIANTE";
        String tipo_mesa2 = "DOCENTE";
        List<Map<String, Object>> resultado_estudiante = consultasVistaVotos.listarVotosTipoMesa(tipo_mesa);
        List<Map<String, Object>> resultado_docente = consultasVistaVotos.listarVotosTipoMesa(tipo_mesa2);
        List<Map<String, Object>> resultado_total = consultasVistaVotos.listarVotosTotal();

        Long sumValido_E = 0L;
        Long sumBlanco_E = 0L;
        Long sumNulo_E = 0L;

        for (Map<String, Object> row : resultado_estudiante) {
            String tipoVoto = (String) row.get("tipo_voto");
            Long sum = ((Number) row.get("sum")).longValue();
            
            if ("VALIDO".equals(tipoVoto)) {
                sumValido_E = sum;
            } else if ("BLANCO".equals(tipoVoto)) {
                sumBlanco_E = sum;
            } else if ("NULO".equals(tipoVoto)) {
                sumNulo_E = sum;
            }
        }

        model.addAttribute("sumValido_E", sumValido_E);
        model.addAttribute("sumBlanco_E", sumBlanco_E);
        model.addAttribute("sumNulo_E", sumNulo_E);

        Long sumValido_D = 0L;
        Long sumBlanco_D = 0L;
        Long sumNulo_D = 0L;

        for (Map<String, Object> row : resultado_docente) {
            String tipoVoto = (String) row.get("tipo_voto");
            Long sumD = ((Number) row.get("sum")).longValue();
            
            if ("VALIDO".equals(tipoVoto)) {
                sumValido_D = sumD;
            } else if ("BLANCO".equals(tipoVoto)) {
                sumBlanco_D = sumD;
            } else if ("NULO".equals(tipoVoto)) {
                sumNulo_D = sumD;
            }
        }

        model.addAttribute("sumValido_D", sumValido_D);
        model.addAttribute("sumBlanco_D", sumBlanco_D);
        model.addAttribute("sumNulo_D", sumNulo_D);

        Long sumValido_T = 0L;
        Long sumBlanco_T = 0L;
        Long sumNulo_T = 0L;

        for (Map<String, Object> row : resultado_total) {
            String tipoVoto = (String) row.get("tipo_voto");
            Long sumD = ((Number) row.get("sum")).longValue();
            
            if ("VALIDO".equals(tipoVoto)) {
                sumValido_T = sumD;
            } else if ("BLANCO".equals(tipoVoto)) {
                sumBlanco_T = sumD;
            } else if ("NULO".equals(tipoVoto)) {
                sumNulo_T = sumD;
            }
        }

        model.addAttribute("sumValido_T", sumValido_T);
        model.addAttribute("sumBlanco_T", sumBlanco_T);
        model.addAttribute("sumNulo_T", sumNulo_T);

        return "publico/vista";
    }

    /* obtener votos por tipo_mesa y id_facultad */
    @PostMapping("/formularioFacultad/{id_facultad}")
    @ResponseBody
    public Map<String, Object> formularioFacultad(HttpServletRequest request, Model model,
            @PathVariable("id_facultad") Long id_facultad) {

                System.out.println("ID Facultad recibido en backend: " + id_facultad);
        Facultad facultad = facultadService.findById(id_facultad);
        String tipo_mesa_e = "ESTUDIANTE";
        String tipo_mesa_d = "DOCENTE";
        List<Map<String, Object>> resultado_e = consultasVistaVotos.votosTipoMEsaFacultad(id_facultad, tipo_mesa_e);
        List<Map<String, Object>> resultado_d = consultasVistaVotos.votosTipoMEsaFacultad(id_facultad, tipo_mesa_d);

        Long sumValido_EF = 0L;
        Long sumBlanco_EF = 0L;
        Long sumNulo_EF = 0L;

        for (Map<String, Object> row : resultado_e) {
            String tipoVoto = (String) row.get("tipo_voto");
            Long sumEF = ((Number) row.get("sum")).longValue();
            
            if ("VALIDO".equals(tipoVoto)) {
                sumValido_EF = sumEF;
            } else if ("BLANCO".equals(tipoVoto)) {
                sumBlanco_EF = sumEF;
            } else if ("NULO".equals(tipoVoto)) {
                sumNulo_EF = sumEF;
            }
        }

        System.out.println("facultad> " + facultad.getNombre_facultad());
        System.out.println("VOTOS FACULTDAD ESTUDIANTE");
        System.out.println(sumValido_EF);
        System.out.println(sumNulo_EF);
        System.out.println(sumBlanco_EF);

        Long sumValido_DF = 0L;
        Long sumBlanco_DF = 0L;
        Long sumNulo_DF = 0L;

        for (Map<String, Object> row : resultado_d) {
            String tipoVoto = (String) row.get("tipo_voto");
            Long sumDF = ((Number) row.get("sum")).longValue();
            
            if ("VALIDO".equals(tipoVoto)) {
                sumValido_DF = sumDF;
            } else if ("BLANCO".equals(tipoVoto)) {
                sumBlanco_DF = sumDF;
            } else if ("NULO".equals(tipoVoto)) {
                sumNulo_DF = sumDF;
            }
        }

        System.out.println("VOTOS FACULTDAD ESTUDIANTE");
        System.out.println(sumValido_DF);
        System.out.println(sumBlanco_DF);
        System.out.println(sumNulo_DF);

        Map<String, Object> result = new HashMap<>();
        result.put("sumValido_EF", sumValido_EF);
        result.put("sumBlanco_EF", sumBlanco_EF);
        result.put("sumNulo_EF", sumNulo_EF);
        result.put("sumValido_DF", sumValido_DF);
        result.put("sumBlanco_DF", sumBlanco_DF);
        result.put("sumNulo_DF", sumNulo_DF);

        return result;
    }

    @GetMapping("/tabla-mesa")
    public String tablaMesas(Model model) throws Exception {

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

        model.addAttribute("listaFacultades", facultadService.listarFacultades());
        model.addAttribute("listaMesas", listaMesas);
        model.addAttribute("id_encryptado", encryptedIds);
        //model.addAttribute("listaMesas",iMesaService.findMesasWithRestantes());

        return "publico/vista_mesa";
    }


    @GetMapping("/carrerasPorFacultad/{params}")
    public String carreraPorFacultad(Model model, @PathVariable("params")Long idFacultad) throws Exception {

        model.addAttribute("listaCarreras", carreraService.findByFacultad(idFacultad));

        return "carrera/opcion";
    }

    @GetMapping("/mesasPorCarrera/{params}")
    public String mostrar_carrera(Model model, @PathVariable("params")Long idCarrera) throws Exception {

        List<Mesa> listaMesas = iMesaService.listarMesasPorIdCarrera(idCarrera);
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

        model.addAttribute("listaMesas", listaMesas );

        return "publico/tablaMesas";
    }

}
