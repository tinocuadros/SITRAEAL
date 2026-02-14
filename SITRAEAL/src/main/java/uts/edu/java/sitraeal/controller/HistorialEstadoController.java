package uts.edu.java.sitraeal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uts.edu.java.sitraeal.modelo.Equipo;
import uts.edu.java.sitraeal.modelo.HistorialEstados;
import uts.edu.java.sitraeal.servicio.EquipoService;
import uts.edu.java.sitraeal.servicio.HistorialEstadoService;

@Controller
@RequestMapping("/historial")
public class HistorialEstadoController {
	@Autowired
    private HistorialEstadoService historialService;

    @Autowired
    private EquipoService equipoService;

    // Ruta para ver el historial de un equipo específico
    // Ejemplo: /historial/equipo/5
    
    
    @GetMapping("/equipo/{id}")
    public String verHistorialEquipo(@PathVariable("id") Integer idEquipo, Model model) {
        
        // 1. Obtener los datos del equipo para mostrar el Serial en la cabecera
        Equipo equipo = equipoService.obtEquipoId(idEquipo);
        
        // 2. Obtener la lista de movimientos desde nuestro nuevo servicio
        List<HistorialEstados> listaHistorial = historialService.listarHistorialPorEquipo(idEquipo);
        
        // 3. Pasar los datos a la vista (HTML)
        model.addAttribute("equipo", equipo);
        model.addAttribute("movimientos", listaHistorial);
        model.addAttribute("titulo", "Historial de Movimientos - " + equipo.getSerial());
        
        return "views/equipo/controlEstados"; // Nombre del archivo HTML
    }
	
	
}
