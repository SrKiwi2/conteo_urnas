package com.usic.conteo.controller.usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usic.conteo.anotaciones.ValidarUsuarioAutenticado;
import com.usic.conteo.model.IService.IUsuarioService;
import com.usic.conteo.model.entity.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final IUsuarioService usuarioService;

    @GetMapping("/")
    public String getMethodName() {
        return "redirect:/form-login";
    }

    @GetMapping(value = "/form-login")
    public String formLogin() {

        return "usuario/login";
    }

    @PostMapping("/iniciar-sesion")
    public String iniciarSesion(@RequestParam(value = "usuario") String user,
            @RequestParam(value = "contrasena") String contrasena, Model model, HttpServletRequest request,
            RedirectAttributes flash) {

        // los dos parametros de usuario, contraseña vienen del formulario html
        Usuario usuario = usuarioService.UsuarioyContraseña(user, contrasena);

        if (usuario != null) {
            if (usuario.getEstado().equals("INACTIVO")) {
                return "redirect:/form-login";
            }
            HttpSession sessionAdministrador = request.getSession(true);
            sessionAdministrador.setAttribute("usuario", usuario);
            sessionAdministrador.setAttribute("persona", usuario.getPersona());
            sessionAdministrador.setAttribute("nombre_rol", usuario.getRol().getNombre());

            flash.addAttribute("success", usuario.getPersona().getNombre());

            return "redirect:/adm/inicio";

        } else {
            return "redirect:/form-login";
        }

    }

    @ValidarUsuarioAutenticado
    @RequestMapping("/cerrar_sesion")
    public String cerrarSesion(HttpServletRequest request, RedirectAttributes flash) {
        Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");
        HttpSession sessionAdministrador = request.getSession();
        if (sessionAdministrador != null) {
            sessionAdministrador.invalidate();
            flash.addAttribute("validado", "Se cerro sesion con exito");
            logger.info("Usuario cerro sesión: {}", usuarioLogueado.getPersona().getNombre());
            
        }
        return "redirect:/form-login";
    }
}
