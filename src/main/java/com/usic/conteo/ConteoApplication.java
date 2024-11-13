package com.usic.conteo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.usic.conteo.model.IService.IPersonaService;
import com.usic.conteo.model.IService.IRolService;
import com.usic.conteo.model.IService.IUsuarioService;
import com.usic.conteo.model.entity.Persona;
import com.usic.conteo.model.entity.Rol;
import com.usic.conteo.model.entity.Usuario;

@SpringBootApplication
public class ConteoApplication {

	private static final Logger logger = LoggerFactory.getLogger(ConteoApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(ConteoApplication.class, args);
	}

	@Bean
    ApplicationRunner init(IUsuarioService usuarioService, IPersonaService personaService, IRolService rolService) {
        return args -> {
            logger.info("SISTEMA CONTEO DE URNAS INICIADO....");
            // Verificar y crear roles si no existen
            String[] roles = { "SUPER USUARIO", "ADMINISTRADOR" };
            Rol[] rolObjects = new Rol[roles.length];
            for (int i = 0; i < roles.length; i++) {
                Rol rol = rolService.buscarRolPorNombre(roles[i]);
                if (rol == null) {
                    rol = new Rol();
                    rol.setNombre(roles[i]);
					rol.setEstado("ACTIVO");
                    rolService.save(rol);
                }
                rolObjects[i] = rol;
            }

            // Crear o actualizar la primera persona y su usuario
            String[] cis = { "111", "222" };
            String[] nombres = { "PRIMER USUARIO", "SEGUNDO USUARIO" };
            String[] usuarios = { "admin1", "admin2" };
			String[] password = { "123", "456" };
            for (int i = 0; i < cis.length; i++) {
                // Verificar si la persona ya existe
                Persona persona = personaService.buscarPersonaPorCI(cis[i]);
                if (persona == null) {
                    persona = new Persona();
                    persona.setNombre(nombres[i]);
                    persona.setPaterno("ApellidoP" + (i + 1));
                    persona.setMaterno("ApellidoM" + (i + 1));
                    persona.setCi(cis[i]);
					persona.setEstado("ACTIVO");
                    personaService.save(persona);
                }

                // Verificar si el usuario ya existe
                Usuario usuario = usuarioService.buscarUsuarioPorNombre(usuarios[i]);
                if (usuario == null) {
                    usuario = new Usuario();
                    usuario.setNombre(usuarios[i]);
                    usuario.setPassword(password[i]);
                    usuario.setPersona(persona); // Asociar la persona con el usuario
                    usuario.setRol(rolObjects[i % roles.length]); // Asignar el rol correspondiente
					usuario.setEstado("ACTIVO");
                    usuarioService.save(usuario);
                }
            }
        };
    }
}
