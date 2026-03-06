package uts.edu.java.sitraeal.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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

	
	@GetMapping("/listar") 
	public String listarEmpleados(Model model) {
	    model.addAttribute("empleados", servicie.listar());
	    // Asegúrate de que esta ruta al HTML sea exacta en src/main/resources/templates/
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
	public String guardarEmpleado(@ModelAttribute Empleado empleado, 
	                              @RequestParam("fileCertificado") MultipartFile archivo,
	                              RedirectAttributes flash) {
	    
	    if (!archivo.isEmpty()) {
	        try {
	            
	            String rootPath = new File("target/uploads/certificados").getAbsolutePath();
	            File directory = new File(rootPath);
	            
	            // 2. Crear carpetas si no existen
	            if (!directory.exists()) {
	                directory.mkdirs();
	            }

	            
	            String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
	            Path rutaCompleta = Paths.get(rootPath + File.separator + nombreArchivo);
	            
	            
	            Files.copy(archivo.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);
	            
	            
	            empleado.setArchivoCertificado(nombreArchivo);
	            
	        } catch (IOException e) {
	            flash.addFlashAttribute("error", "Error al procesar el archivo: " + e.getMessage());
	            return "redirect:/empleado/registro";
	        }
	    }

	    try {
	        // Usamos tu servicio 'EmpleadoServiceEmp' ya creado para persistir
	        servicie.guardar(empleado);
	        flash.addFlashAttribute("success", "Empleado y certificado guardados con éxito.");
	    } catch (RuntimeException e) {
	        // Maneja tu excepción personalizada 'EMPLEADO_EXISTE'
	        flash.addFlashAttribute("error", "El ID del empleado ya se encuentra registrado.");
	    }

	    return "redirect:/empleado/listar";
	}

	

}
