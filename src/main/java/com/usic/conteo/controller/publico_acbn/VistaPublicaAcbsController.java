package com.usic.conteo.controller.publico_acbn;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usic.conteo.config.Encriptar;
import com.usic.conteo.model.IService.ICarreraService;
import com.usic.conteo.model.IService.IFacultadService;
import com.usic.conteo.model.IService.IMesaService;
import com.usic.conteo.model.Repository.ConsultasVistaVotos;
import com.usic.conteo.model.entity.Carrera;
import com.usic.conteo.model.entity.Facultad;
import com.usic.conteo.model.entity.Mesa;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/acbn")
@RequiredArgsConstructor
public class VistaPublicaAcbsController {
    
    private final IFacultadService facultadService;
    private final ICarreraService carreraService;
    private final ConsultasVistaVotos consultasVistaVotos;
    private final IMesaService iMesaService;

    @GetMapping(value = "/vista")
    public String vista_acbn(Model model) {

        model.addAttribute("listaFacultades", facultadService.listarFacultades());
        model.addAttribute("listaCarreras", carreraService.listarCarreras());
        return "publico_acbn/vista-acbn";
    }

    @GetMapping("/tabla-mesa")
    public String tablaMesasPorCarerra(@RequestParam("idCarrera") Long idCarrera, Model model) throws Exception {

        List<Mesa> listaMesas = iMesaService.findByCarrera(idCarrera);

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

        return "publico_acbn/vista-mesa-acbn";
    }

    @GetMapping("/graficos")
    public String graficos(Model model) throws Exception {

        List<Carrera> carreras = carreraService.listarCarreras();
        model.addAttribute("listaCarreras", carreras);

        if (!carreras.isEmpty()) {
            Long idPrimeraCarrera = carreras.get(0).getId_carrera();
            model.addAttribute("idPrimeraCarrera", idPrimeraCarrera);
        }

        String tipo_mesa = "ESTUDIANTE";
        String tipo_mesa2 = "DOCENTE";
        String nomnre_frente = "RENOVACION CON CIENCIA";

        List<Map<String, Object>> resultado_estudiante = consultasVistaVotos.listarVotosEstudiantesRenovacion(tipo_mesa, nomnre_frente);
        List<Map<String, Object>> resultado_docente = consultasVistaVotos.listarVotosEstudiantesRenovacion(tipo_mesa2, nomnre_frente);
        List<Map<String, Object>> resultado_total = consultasVistaVotos.listarVotosTotalACBN();

        Long sumRenovacion_E = 0L;
        Long sumACBN_E = 0L;
        Long sumBlanco_E = 0L;
        Long sumNulo_E = 0L;

        for (Map<String, Object> row : resultado_estudiante) {
            String tipoVoto = (String) row.get("tipo_voto_agrupado"); // usa el alias correcto
            Object totalObj = row.get("total_votos");
        
            if (totalObj != null && tipoVoto != null) {
                Long sum = ((Number) totalObj).longValue();
        
                switch (tipoVoto) {
                    case "RENOVACION" -> sumRenovacion_E = sum;
                    case "100% ACBN" -> sumACBN_E = sum;
                    case "BLANCO" -> sumBlanco_E = sum;
                    case "NULO" -> sumNulo_E = sum;
                }
            }
        }

        model.addAttribute("sumRenovacion_E", sumRenovacion_E);
        model.addAttribute("sumACBN_E", sumACBN_E);
        model.addAttribute("sumBlanco_E", sumBlanco_E);
        model.addAttribute("sumNulo_E", sumNulo_E);

        System.out.println("renovacion: " + sumRenovacion_E + " ACBN: " + sumACBN_E + " Blancos " + sumBlanco_E + " Nulos " + sumBlanco_E);


        Long sumRenovacion_D = 0L;
        Long sumACBN_D = 0L;
        Long sumBlanco_D = 0L;
        Long sumNulo_D = 0L;

        for (Map<String, Object> row : resultado_docente) {
            String tipoVoto = (String) row.get("tipo_voto_agrupado"); // usa el alias correcto
            Object totalObj = row.get("total_votos");
        
            if (totalObj != null && tipoVoto != null) {
                Long sum = ((Number) totalObj).longValue();
        
                switch (tipoVoto) {
                    case "RENOVACION" -> sumRenovacion_D = sum;
                    case "100% ACBN" -> sumACBN_D = sum;
                    case "BLANCO" -> sumBlanco_D = sum;
                    case "NULO" -> sumNulo_D = sum;
                }
            }
        }

        model.addAttribute("sumRenovacion_D", sumRenovacion_D);
        model.addAttribute("sumACBN_D", sumACBN_D);
        model.addAttribute("sumBlanco_D", sumBlanco_D);
        model.addAttribute("sumNulo_D", sumNulo_D);

        System.out.println("renovacion: " + sumRenovacion_D + " ACBN: " + sumACBN_D + " Blancos " + sumBlanco_D + " Nulos " + sumNulo_D);

        BigDecimal sumEstudiantesRenovacion = BigDecimal.ZERO;
        BigDecimal sumEstudiantesACBN = BigDecimal.ZERO;
        BigDecimal sumEstudiantesBlanco = BigDecimal.ZERO;
        BigDecimal sumEstudiantesNulo = BigDecimal.ZERO;

        BigDecimal sumDocentesRenovacion = BigDecimal.ZERO;
        BigDecimal sumDocentesACBN = BigDecimal.ZERO;
        BigDecimal sumDocentesBlanco = BigDecimal.ZERO;
        BigDecimal sumDocentesNulo = BigDecimal.ZERO;

        BigDecimal totalEstudiantes = BigDecimal.ZERO;
        BigDecimal totalDocentes = BigDecimal.ZERO;

        for (Map<String, Object> row : resultado_total) {
            String tipoVoto = (String) row.get("tipo_voto_agrupado");
            String tipoMesa = (String) row.get("tipo_mesa");  // Obtener tipo de mesa (Estudiante o Docente)
            Object totalObj = row.get("total_votos");
            
            if (totalObj != null && tipoVoto != null && tipoMesa != null) {
                BigDecimal sum = new BigDecimal(((Number) totalObj).longValue());
                
                if (tipoMesa.equals("ESTUDIANTE")) {
                    totalEstudiantes = totalEstudiantes.add(sum);  // Sumar votos de estudiantes
                    switch (tipoVoto) {
                        case "RENOVACION" -> sumEstudiantesRenovacion = sum;
                        case "100% ACBN" -> sumEstudiantesACBN = sum;
                        case "BLANCO" -> sumEstudiantesBlanco = sum;
                        case "NULO" -> sumEstudiantesNulo = sum;
                    }
                } else if (tipoMesa.equals("DOCENTE")) {
                    totalDocentes = totalDocentes.add(sum);  // Sumar votos de docentes
                    switch (tipoVoto) {
                        case "RENOVACION" -> sumDocentesRenovacion = sum;
                        case "100% ACBN" -> sumDocentesACBN = sum;
                        case "BLANCO" -> sumDocentesBlanco = sum;
                        case "NULO" -> sumDocentesNulo = sum;
                    }
                }
            }
        }
        System.out.println();
        
        // Calcular la equivalencia de votos entre estudiantes y docentes
        BigDecimal equivalenciaDocenteAEstudiante = totalEstudiantes.divide(totalDocentes, 8, RoundingMode.HALF_UP);  // Mantener 5 decimales
        System.out.println("Voto docente euivalente a votos estudiantes: "+ equivalenciaDocenteAEstudiante);

        // Calcular los votos de docentes equivalentes a votos de estudiantes
        BigDecimal votosDocentesRenovacionEquivalentes = sumDocentesRenovacion.multiply(equivalenciaDocenteAEstudiante);
        System.out.println("Voto renovacion: "+ votosDocentesRenovacionEquivalentes);
        BigDecimal votosDocentesACBNEquivalentes = sumDocentesACBN.multiply(equivalenciaDocenteAEstudiante);
        System.out.println("Voto acbn: "+ votosDocentesACBNEquivalentes);
        BigDecimal votosDocentesBlancoEquivalentes = sumDocentesBlanco.multiply(equivalenciaDocenteAEstudiante);
        System.out.println("Voto blanco: "+ votosDocentesBlancoEquivalentes);
        BigDecimal votosDocentesNuloEquivalentes = sumDocentesNulo.multiply(equivalenciaDocenteAEstudiante);
        System.out.println("Voto nulo: "+ votosDocentesNuloEquivalentes);

        System.out.println("voto estudinate renovacion: "+sumEstudiantesRenovacion);
        System.out.println("voto estudinate acbn: "+sumEstudiantesACBN);
        System.out.println("voto estudinate blanco: "+sumEstudiantesBlanco);
        System.out.println("voto estudinate nulo: "+sumEstudiantesNulo);
        
        // Calcular los votos promedios entre docentes y estudiantes
        BigDecimal promedioRenovacion = (sumEstudiantesRenovacion.add(votosDocentesRenovacionEquivalentes)).divide(new BigDecimal(2), 5, RoundingMode.HALF_UP);
        BigDecimal promedioACBN = (sumEstudiantesACBN.add(votosDocentesACBNEquivalentes)).divide(new BigDecimal(2), 5, RoundingMode.HALF_UP);
        BigDecimal promedioBlanco = (sumEstudiantesBlanco.add(votosDocentesBlancoEquivalentes)).divide(new BigDecimal(2), 5, RoundingMode.HALF_UP);
        BigDecimal promedioNulo = (sumEstudiantesNulo.add(votosDocentesNuloEquivalentes)).divide(new BigDecimal(2), 5, RoundingMode.HALF_UP);

        // Agregar atributos al modelo
        model.addAttribute("sumRenovacion_T", promedioRenovacion);
        model.addAttribute("sumACBN_T", promedioACBN);
        model.addAttribute("sumBlanco_T", promedioBlanco);
        model.addAttribute("sumNulo_T", promedioNulo);
        model.addAttribute("total_estudiantes", totalEstudiantes);
        System.out.println("renovacion: " + promedioRenovacion + " ACBN: " + promedioACBN + " Blancos " + promedioBlanco + " Nulos " + promedioNulo + "toTAL ESTUDINATE: " + totalEstudiantes);

        return "publico_acbn/graficos";
    }

    @GetMapping("/facultad/{id}")
    @ResponseBody
    public List<Carrera> obtenerCarrerasPorFacultad(@PathVariable Long id) {
        return carreraService.findByFacultadId(id);
    }

    @GetMapping("/carrerasPorFacultad/{params}")
    public String carreraPorFacultad_acbn(Model model, @PathVariable("params")Long idFacultad) throws Exception {

        model.addAttribute("listaCarreras", carreraService.findByFacultadId(idFacultad));
        return "carrera/opcion";
    }

    @GetMapping("/mesasPorCarrera/{params}")
    public String mostrar_carrera(Model model, @PathVariable("params")Long idCarrera) throws Exception {

        String tipo_mesa = "ESTUDIANTE";
        String tipo_mesa2 = "DOCENTE";
        List<Map<String, Object>> resultado_estudiante = consultasVistaVotos.votosPorCarrera(idCarrera, tipo_mesa);
        List<Map<String, Object>> resultado_docente = consultasVistaVotos.votosPorCarrera(idCarrera, tipo_mesa2);
        
        Carrera carrera = carreraService.findById(idCarrera);
        
        List<Mesa> listaMesas = iMesaService.findByCarrera(idCarrera);
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

        Long sumMesasValidoE = 0L;
        Long sumMesasNuloE = 0L;
        Long sumMesasVacioE = 0L;

        Long sumMesasValidoD = 0L;
        Long sumMesasNuloD = 0L;
        Long sumMesasVacioD = 0L;

        for (Map<String, Object> row : resultado_estudiante) {
            String tipoVoto = (String) row.get("tipo_voto");
            Long sumE = ((Number) row.get("sum")).longValue();
            
            if ("VALIDO".equals(tipoVoto)) {
                sumMesasValidoE = sumE;
            } else if ("BLANCO".equals(tipoVoto)) {
                sumMesasVacioE = sumE;
            } else if ("NULO".equals(tipoVoto)) {
                sumMesasNuloE = sumE;
            }
        }

        for (Map<String, Object> row : resultado_docente) {
            String tipoVoto = (String) row.get("tipo_voto");
            Long sumD = ((Number) row.get("sum")).longValue();
            
            if ("VALIDO".equals(tipoVoto)) {
                sumMesasValidoD = sumD;
            } else if ("BLANCO".equals(tipoVoto)) {
                sumMesasVacioD = sumD;
            } else if ("NULO".equals(tipoVoto)) {
                sumMesasNuloD = sumD;
            }
        }

        model.addAttribute("sumMesasValidoE", sumMesasValidoE);
        model.addAttribute("sumMesasVacioE", sumMesasVacioE);
        model.addAttribute("sumMesasNuloE", sumMesasNuloE);

        System.out.println(carrera.getNombre_carrera());
        System.out.println(resultado_estudiante);
        System.out.println(sumMesasValidoE);
        System.out.println(sumMesasVacioE);
        System.out.println(sumMesasNuloE);

        model.addAttribute("sumMesasValidoD", sumMesasValidoD);
        model.addAttribute("sumMesasVacioD", sumMesasVacioD);
        model.addAttribute("sumMesasNuloD", sumMesasNuloD);

        System.out.println(resultado_docente);
        System.out.println(sumMesasValidoD);
        System.out.println(sumMesasVacioD);
        System.out.println(sumMesasNuloD);

        model.addAttribute("listaMesas", listaMesas );

        return "publico_acbn/mesa-carrera-acbn";
    }
}
