package com.usic.conteo.config;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.usic.conteo.anotaciones.ValidarUsuarioAutenticado;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class UsuarioAutenticadoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (handler instanceof HandlerMethod handlerMethod) {
            ValidarUsuarioAutenticado anotacion = handlerMethod.getMethodAnnotation(ValidarUsuarioAutenticado.class);
            if (anotacion != null) {
                HttpSession session = request.getSession();

                if (session.getAttribute("persona") == null) {
                    response.sendRedirect("/form-login");
                    return false;
                }
            }
        }
        return true;
    }
}