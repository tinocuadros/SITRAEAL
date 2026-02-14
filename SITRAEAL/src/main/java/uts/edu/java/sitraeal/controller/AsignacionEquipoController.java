package uts.edu.java.sitraeal.controller;

import java.security.Provider.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uts.edu.java.sitraeal.modelo.AsignacionEquipo;
import uts.edu.java.sitraeal.repositorio.EmpleadoRepository;
import uts.edu.java.sitraeal.repositorio.EquipoRepository;
import uts.edu.java.sitraeal.servicio.AsignacionEquipoService;


@Controller
@RequestMapping("/asignacion")
public class AsignacionEquipoController {

    private final EmpleadoRepository empleadoRepository;

    private final EquipoRepository equipoRepository;

	@Autowired
    private AsignacionEquipoService asignacionService;

    AsignacionEquipoController(EquipoRepository equipoRepository, EmpleadoRepository empleadoRepository) {
        this.equipoRepository = equipoRepository;
        this.empleadoRepository = empleadoRepository;
    }
	
    
    //Metodo para listar atributos 
	@ModelAttribute
	public void cargarListas(Model model) {
	    model.addAttribute("Equipo", equipoRepository.findAll());
	    model.addAttribute("Empleado", empleadoRepository.findAll());
	   
	}
	
	//listar
	/*@GetMapping
	public String ListarAsignacion(Model model) {
		
		model.addAttribute("AsignacionEquipo", asignacionService.listar());
        return "views/asignacion/listarAsignacion";
    }*/
	
	//Mostar Formulario
	@GetMapping("/nuevo")
	public String mostarFormulario(Model model) {
		model.addAttribute("asignacion", new AsignacionEquipo());
		
		return "views/asignacion/formAsignacion";
	}
	
	@PostMapping("/guardar")
	public String guardar(@ModelAttribute("asignacion") AsignacionEquipo asignacionEquipo, RedirectAttributes redirectAttributes) {
	    try {
	       
	        asignacionService.guardar(asignacionEquipo);
	        
	        // Usamos redirectAttributes 
	        redirectAttributes.addFlashAttribute("msgSuccess", "¡Equipo asignado exitosamente!");
	        return "redirect:/asignacion/nuevo"; 
	        
	    } catch (RuntimeException e) {
	        // Capturamos el error 
	        redirectAttributes.addFlashAttribute("msgError", e.getMessage());
	        return "redirect:/asignacion/nuevo";
	    }
	}
	
	@GetMapping("/editar/{id}")
	public String devolverEquipo(@PathVariable("id") Integer idAsignacion, RedirectAttributes flash) {
	    try {
	        // Llamamos al método editar 
	        asignacionService.editar(idAsignacion, null);
	        
	        flash.addFlashAttribute("msgSuccess", "Equipo devuelto correctamente. Ahora está disponible.");
	    } catch (Exception e) {
	        flash.addFlashAttribute("msgError", "Error al devolver: " + e.getMessage());
	    }
	    return "redirect:/asignacion"; 
	}
	
	//Listar por estado
	@GetMapping
	public String listarAsignacion(Model model) {
	    // CAMBIO: En lugar de listar(), usamos el nuevo filtro
	    model.addAttribute("AsignacionEquipo", asignacionService.listarSoloAsignados());
	    return "views/asignacion/listarAsignacion";
	}
}
