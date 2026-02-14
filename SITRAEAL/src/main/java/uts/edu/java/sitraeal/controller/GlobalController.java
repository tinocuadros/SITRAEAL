package uts.edu.java.sitraeal.controller;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice; // IMPORTANTE
import org.springframework.web.bind.annotation.ModelAttribute;
import uts.edu.java.sitraeal.security.CustomUserDetails;
import uts.edu.java.sitraeal.modelo.Equipo;
import uts.edu.java.sitraeal.repositorio.EquipoRepository;

@ControllerAdvice
public class GlobalController {

    @Autowired
    private EquipoRepository equipoRepository; // Necesitas inyectar tu repositorio de equipos

    @ModelAttribute
    public void cargarDatosGlobales(Model model, Authentication auth) {
        // 1. Cargar Usuario (Lo que ya tenías)
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {
            model.addAttribute("usuarioLogueado", user.getUsuario());
        }

        // 2. Cargar Alertas de Equipos
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(15);

        // Buscamos equipos cuya fecha de vencimiento sea menor al límite (15 días)
        List<Equipo> equiposAlerta = equipoRepository.findByFechaVencimientoBefore(limite);
        
        // Contamos cuántos de esos ya están vencidos (para poner la campana en ROJO)
        boolean hayVencidos = equiposAlerta.stream()
                .anyMatch(e -> e.getFechaVencimiento().isBefore(hoy));

        model.addAttribute("equiposAlerta", equiposAlerta);
        model.addAttribute("totalAlertas", equiposAlerta.size());
        model.addAttribute("hayVencidos", hayVencidos);
    }
}