package com.usic.conteo.controller.docente;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.usic.conteo.model.IService.INacionalidadService;
import com.usic.conteo.model.IService.IPersonaService;
import com.usic.conteo.model.IService.ISexoService;
import com.usic.conteo.model.entity.Nacionalidad;
import com.usic.conteo.model.entity.Persona;
import com.usic.conteo.model.entity.Sexo;
import com.usic.conteo.model.entity.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/docente")
@RequiredArgsConstructor
public class Docentecontroller {

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private INacionalidadService nacionalidadService;

    @Autowired
    private ISexoService sexoService;
    
    @PostMapping("/api_docente")
    public ResponseEntity<String> buscarDocenteApi(HttpServletRequest request, @RequestParam(name = "rd") String rd) {
        Map<String, String> response = new HashMap<>();
        try{
            String url = "http://190.129.216.246:9993/v1/service/api/ae7ce0054d4c4f38a4a92bf1c0422b55";
            String key = "key 70c8b6fc339aa5e6312dd42edf0636558948bb6008f1a0f867885d5e60e26c57";

            HttpHeaders headers =new HttpHeaders();
            headers.set("x-api-key", key);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>("{\"rd\":" + rd + "}", headers);

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
