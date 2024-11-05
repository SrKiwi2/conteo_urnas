package com.usic.conteo.controller.estudiante;



import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.usic.conteo.model.IService.IEstudianteService;
import com.usic.conteo.model.IService.INacionalidadService;
import com.usic.conteo.model.IService.IPersonaService;
import com.usic.conteo.model.IService.ISexoService;
import com.usic.conteo.model.entity.Carrera;
import com.usic.conteo.model.entity.Estudiante;
import com.usic.conteo.model.entity.Nacionalidad;
import com.usic.conteo.model.entity.Persona;
import com.usic.conteo.model.entity.Sexo;
import com.usic.conteo.model.entity.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/estudiante")
@RequiredArgsConstructor
public class EstudianteController {

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private INacionalidadService nacionalidadService;

    @Autowired
    private ISexoService sexoService;

    @Autowired
    private IEstudianteService estudianteService;
    
    @PostMapping("/api_estudiante")
    public ResponseEntity<String> buscarEstudianteApi(HttpServletRequest request, @RequestParam(name = "ru") String ru) {
        Map<String, String> response = new HashMap<>();
        try{
            String url = "http://190.129.216.246:9993/v1/service/api/f570d1184e724359a59f03211a1a9f50";
            String key = "key 9e0adaab85cc224bed37bb2eb1bb61f6ce020bec6f99a872fbb23ec23b6f2449";

            HttpHeaders headers =new HttpHeaders();
            headers.set("x-api-key", key);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>("{\"ru\":" + ru + "}", headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
                Map<String, Object> data = (Map<String, Object>) responseEntity.getBody().get("data");

                Persona persona_encontrada = personaService.buscarPersonaPorCI(data.get("ci").toString()); 
                Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
                
                if (persona_encontrada == null) {
                    Persona persona = new Persona();
                    persona.setCi(data.get("ci").toString());
                    persona.setNombre(data.get("nombres").toString());
                    persona.setPaterno(data.get("apellido_paterno").toString());
                    persona.setMaterno(data.get("apellido_materno").toString());
                    persona.setRegistroIdUsuario(usuario.getIdUsuario());

                    Nacionalidad nacionalidad_encontrada = nacionalidadService.buscarNacionalidadPorNombre(data.get("nacionalidad").toString());
                    if (nacionalidad_encontrada == null) {
                        Nacionalidad nacionalidad = new Nacionalidad();
                        nacionalidad.setNombre(data.get("nacionalidad").toString());
                        nacionalidad.setRegistroIdUsuario(usuario.getIdUsuario());
                        nacionalidadService.save(nacionalidad);
                        persona.setNacionalidad(nacionalidad);
                    }
                    
                    Sexo sexo_encontrado = sexoService.buscarSexoPorNombre(data.get("sexo").toString());
                    if (sexo_encontrado == null) {
                        Sexo sexo = new Sexo();
                        sexo.setNombre(data.get("sexo").toString());
                        sexo.setRegistroIdUsuario(usuario.getIdUsuario());
                        sexoService.save(sexo);
                        persona.setSexo(sexo);
                    }
                    persona.setEstado("ACTIVO");
                    personaService.save(persona);

                    Estudiante estudiante_encontrado = estudianteService.buscarEstudinatePorRU(ru);
                    if (estudiante_encontrado == null) {
                        Estudiante estudiante = new Estudiante();
                        estudiante.setPersona(persona);
                        estudiante.setRu(ru);
                        estudiante.setTipo_carrera(data.get("tipo_carrera").toString());
                        
                    }

                    return ResponseEntity.ok("Se realizó el registro correctamente");
                }else{
                    return ResponseEntity.ok("Ya existe una especialidad con este nombre");
                }

            }else {
                response.put("error", "No se pudo obtener los datos del Estudiante");
                return ResponseEntity.ok("No se pudo obtener los datos del Estudiante");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Error al procesar la solicitud");
            return ResponseEntity.ok("Error al procesar la solicitud");
        }
    }
}

/*
 *  datos de estudiantes
                response.put("ru", data.get("ru").toString());
                response.put("tipoCarrera", data.get("tipo_carrera").toString());
                response.put("plan", data.get("plan").toString());
                response.put("carrera", data.get("carrera").toString());
 * 
 */
