package uts.edu.java.sitraeal.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uts.edu.java.sitraeal.modelo.ReporteIncidente;
import uts.edu.java.sitraeal.servicio.EmpleadoServicie;
import uts.edu.java.sitraeal.servicio.EquipoService;
import uts.edu.java.sitraeal.servicio.ReporteIncidenteService;

@Controller
@RequestMapping("/reporteIncidente")
public class ReporteIncidenteController {

    private final ReporteIncidenteService reporteService;
    private final EquipoService equipoService;
    private final EmpleadoServicie empleadoService;

    // Inyectamos todos los servicios necesarios en el constructor
    public ReporteIncidenteController(ReporteIncidenteService reporteService, 
                                     EquipoService equipoService, 
                                     EmpleadoServicie empleadoService) {
        this.reporteService = reporteService;
        this.equipoService = equipoService;
        this.empleadoService = empleadoService;
    }

    // LISTAR REPORTES
    @GetMapping
    public String listarReporte(Model model) {
        model.addAttribute("reportes", reporteService.listar());
        return "views/reporteIncidente/listarReportes";
    }

    // MOSTRAR FORMULARIO DE NUEVO REPORTE
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        // CREAR EL OBJETO E INICIALIZAR SUS RELACIONES
        ReporteIncidente reporte = new ReporteIncidente();
        reporte.setEquipo(new uts.edu.java.sitraeal.modelo.Equipo()); 
        reporte.setEmpleado(new uts.edu.java.sitraeal.modelo.Empleado());
        
        model.addAttribute("reporte", reporte);
        model.addAttribute("equipos", equipoService.listar());
        model.addAttribute("empleados", empleadoService.listar());
        
        // Asegúrate de que esta ruta coincida con tu carpeta en src/main/resources/templates
        return "views/reporteIncidente/formReporte";
    }

    // GUARDAR REPORTE
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("reporte") ReporteIncidente reporte, Model model) {
        try {
            // 1. Guardar el reporte
            reporteService.guardar(reporte);
            
            // 2. Mensaje de éxito
            model.addAttribute("msgSuccess", "¡Reporte registrado exitosamente!");
            
            // 3. Limpiar el objeto para un nuevo registro o dejar el actual
            model.addAttribute("reporte", new ReporteIncidente()); 
            
        } catch (Exception e) {
            model.addAttribute("msgError", "Error al registrar: " + e.getMessage());
            model.addAttribute("reporte", reporte);
        }
        
        model.addAttribute("equipos", equipoService.listar());
        model.addAttribute("empleados", empleadoService.listar());
        
        return "views/reporteIncidente/formReporte";
    }
}