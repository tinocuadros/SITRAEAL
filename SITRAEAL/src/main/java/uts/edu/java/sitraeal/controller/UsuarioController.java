package uts.edu.java.sitraeal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uts.edu.java.sitraeal.modelo.Usuario;
import uts.edu.java.sitraeal.servicio.UsuarioService;


@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    // LISTAR USUARIOS
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", service.listar());
        return "views/usuario/index";
    }

    // MOSTRAR FORMULARIO
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "views/usuario/form";
    }

    // GUARDAR USUARIO (MOSTRAR MENSAJES)
    @PostMapping
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {

        try {
            service.guardar(usuario);
            model.addAttribute("msgSuccess", "Usuario registrado correctamente");

            // Limpiar formulario
            model.addAttribute("usuario", new Usuario());

        } catch (RuntimeException e) {

            if ("USUARIO_EXISTE".equals(e.getMessage())) {
                model.addAttribute("msgError", "El usuario ya existe");
            } else {
                model.addAttribute("msgError", "Error al guardar el usuario");
            }
        }

        return "views/usuario/form";
    }
}
