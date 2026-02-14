package uts.edu.java.sitraeal.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import uts.edu.java.sitraeal.modelo.Usuario;
import uts.edu.java.sitraeal.repositorio.UsuarioRepository;

@Controller
public class UsuarioSessionController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioSessionController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @ModelAttribute("usuarioLogueado")
    public Usuario usuarioLogueado(Authentication authentication) {

        if (authentication == null) {
            return null;
        }

        String correo = authentication.getName();
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }
}

