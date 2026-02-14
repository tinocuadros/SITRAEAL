package uts.edu.java.sitraeal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uts.edu.java.sitraeal.modelo.Empleado;
import uts.edu.java.sitraeal.repositorio.CiudadRepository;
import uts.edu.java.sitraeal.servicio.EmpleadoServicie;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {

    private final CiudadRepository ciudadRepository;

	private final EmpleadoServicie servicie;

	public EmpleadoController(EmpleadoServicie servicie, CiudadRepository ciudadRepository) {
		this.servicie = servicie;
		this.ciudadRepository = ciudadRepository;
	}

	// LISTAR EMPLEADOS
	@GetMapping
	public String listarEmpleados(Model model) {
		model.addAttribute("empleados", servicie.listar());
		return "views/empleado/listarEmpleado";
	}

	// MOSTRAR FORMULARIO
	@GetMapping("/nuevo")
	public String mostrarFormulario(Model model) {
		model.addAttribute("empleado", new Empleado());
		model.addAttribute("ciudades", ciudadRepository.findAll());
		return "views/empleado/formEmpleado";
	}

	// EDICTAR EMPLEADO
	@GetMapping("/editar/{idEmpleado}")
	public String editarEmpleado(@PathVariable Integer idEmpleado, Model model) {

		Empleado empleado = servicie.obtEmpleadoId(idEmpleado);
		model.addAttribute("empleado", empleado);

		return "views/empleado/formEmpleado";
	}

	// GUARDAR EMPLEADO
	@PostMapping("/guardar")
	public String guardarEmpleado(@ModelAttribute("empleado") Empleado empleado, RedirectAttributes redirectAttributes) {
	    try {
	        servicie.guardar(empleado);
	        redirectAttributes.addFlashAttribute("msgSuccess", "Empleado registrado correctamente");
	        return "redirect:/empleado/nuevo";  // Redirige al formulario de nuevo empleado
	    } catch (Exception e) {
	        if ("EMPLEADO_EXISTE".equals(e.getMessage())) {
	            redirectAttributes.addFlashAttribute("msgError", "El empleado ya existe");
	        } else {
	            redirectAttributes.addFlashAttribute("msgError", "Error al guardar el empleado");
	        }
	        return "redirect:/empleado/nuevo";  // Redirige de nuevo al formulario
	    }
	}

	

}
