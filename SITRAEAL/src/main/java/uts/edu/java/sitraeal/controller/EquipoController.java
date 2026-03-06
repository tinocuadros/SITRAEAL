package uts.edu.java.sitraeal.controller;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import uts.edu.java.sitraeal.modelo.Equipo;
import uts.edu.java.sitraeal.modelo.Usuario;
import uts.edu.java.sitraeal.repositorio.*;
import uts.edu.java.sitraeal.servicio.EquipoService;

@Controller
@RequestMapping("/equipo")
public class EquipoController {

	private final CategoriaEquipoRepository categoriaEquipoRepository;

	private final EstadoEquipoRepository estadoEquipoRepository;

	private final ProveedorRepository proveedorRepository;

	private final NormaRepository normaRepository;

	private final MarcaEquipoRepository marcaEquipoRepository;

	private final TipoEquipoRepository tipoEquipoRepository;

	private final EquipoRepository equipoRepository;

	private final EquipoService service;

	EquipoController(EquipoRepository equipoRepository, TipoEquipoRepository tipoEquipoRepository,
			MarcaEquipoRepository marcaEquipoRepository, NormaRepository normaRepository,
			ProveedorRepository proveedorRepository, EstadoEquipoRepository estadoEquipoRepository,
			CategoriaEquipoRepository categoriaEquipoRepository, EquipoService service) {
		this.equipoRepository = equipoRepository;
		this.tipoEquipoRepository = tipoEquipoRepository;
		this.marcaEquipoRepository = marcaEquipoRepository;
		this.normaRepository = normaRepository;
		this.proveedorRepository = proveedorRepository;
		this.estadoEquipoRepository = estadoEquipoRepository;
		this.categoriaEquipoRepository = categoriaEquipoRepository;
		this.service = service;
	}

	// LISTAR EQUIPOS
	@GetMapping
	public String listarEquipo(Model model) {
		model.addAttribute("equipo", service.listar());

		return "views/equipo/listarEquipo";
	}

	// MOSTAR FORMULARIO
	@GetMapping("/nuevo")
	public String mostarFormulario(Model model) {
		model.addAttribute("equipo", new Equipo());
		return "views/equipo/formEquipo";
	}
	
	//Listar Modelos
	@ModelAttribute
	public void cargarListas(Model model) {
	    model.addAttribute("CategoriaEquipo", categoriaEquipoRepository.findAll());
	    model.addAttribute("TipoEquipo", tipoEquipoRepository.findAll());
	    model.addAttribute("MarcaEquipo", marcaEquipoRepository.findAll());
	    model.addAttribute("Norma", normaRepository.findAll());
	    model.addAttribute("Proveedor", proveedorRepository.findAll());
	    model.addAttribute("EstadoEquipo", estadoEquipoRepository.findAll());
	}

	// EDICTAR 
	@GetMapping("/editar/{idEquipo}")
	public String editarEquipo(@PathVariable Integer idEquipo, Model model) {

		Equipo equipo = service.obtEquipoId(idEquipo);
		model.addAttribute("equipo", equipo);
		

		return "views/equipo/formEquipo";

	}

	
	@PostMapping("/guardar")
	public String guardarEquipo(
	        @ModelAttribute("equipo") Equipo equipo,
	        RedirectAttributes redirectAttributes) {

	    try {
	        // 1. Normalización del Serial (Pasar a MAYÚSCULAS y quitar espacios)
	        if (equipo.getSerial() != null) {
	            equipo.setSerial(equipo.getSerial().toUpperCase().trim());
	        }

	        // 2. Datos automáticos
	        equipo.setFechaIngreso(LocalDateTime.now());

	        // 3. Guardado en la Base de Datos
	        service.guardar(equipo);

	        redirectAttributes.addFlashAttribute("msgSuccess", "Equipo registrado correctamente.");
	        return "redirect:/equipo/nuevo";

	    } catch (Exception e) {
	        // Captura cualquier error de base de datos o validación
	        redirectAttributes.addFlashAttribute("msgError", "Error al guardar el equipo: " + e.getMessage());
	        return "redirect:/equipo/nuevo";
	    }
	}
	//Cambiar estados 
	@GetMapping("/controlEstados")
	public String mostrarControlEstados(
	        @RequestParam(value = "criterio", required = false) String criterio, 
	        Model model) {
	    
	    model.addAttribute("estados", estadoEquipoRepository.findAll());

	    if (criterio != null && !criterio.isEmpty()) {
	        // 1. Buscamos SIEMPRE primero por Serial (sea numérico o no)
	        Equipo equipo = service.buscarPorSerial(criterio.trim().toUpperCase());
	        
	        // 2. Si no se encontró por serial, intentamos ver si el criterio es un ID numérico
	        if (equipo == null) {
	            try {
	                Integer id = Integer.parseInt(criterio);
	                equipo = service.obtEquipoId(id);
	            } catch (NumberFormatException e) {
	                // No es número y no se encontró por serial
	            }
	        }

	        if (equipo != null) {
	            model.addAttribute("equipo", equipo);
	        } else {
	            model.addAttribute("error", "No se encontró equipo con Serial o ID: " + criterio);
	        }
	    }
	    
	    return "views/equipo/controlEstados"; 
	}
	
	
	//actualizar estados 
	@PostMapping("/actualizar-estado-completo")
	public String actualizarEstadoCompleto(
	        @RequestParam("idEquipo") Integer id,
	        @RequestParam("idEstado") Integer idEstado,
	        @RequestParam("fechaCertificacion") String fCert,
	        @RequestParam("fechaVencimiento") String fVenc,
	        @RequestParam("observaciones") String obs,
	        @RequestParam("fileCertificacion") org.springframework.web.multipart.MultipartFile archivo, 
	        RedirectAttributes ra) {

	    try {
	        String nombreArchivo = null;

	        // 1. Lógica de carga de archivo
	        if (!archivo.isEmpty()) {
	            String rootPath = new File("target/uploads/recertificaciones").getAbsolutePath();
	            File directory = new File(rootPath);
	            if (!directory.exists()) directory.mkdirs();

	            nombreArchivo = java.util.UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
	            java.nio.file.Path rutaCompleta = java.nio.file.Paths.get(rootPath + File.separator + nombreArchivo);
	            
	            java.nio.file.Files.copy(archivo.getInputStream(), rutaCompleta, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
	        }

	        LocalDate cert = (fCert != null && !fCert.isEmpty()) ? LocalDate.parse(fCert) : null;
	        LocalDate venc = (fVenc != null && !fVenc.isEmpty()) ? LocalDate.parse(fVenc) : null;

	       
	        // Se cambia 'fCert' por 'nombreArchivo' para que guarde el nombre del archivo en la BD
	        service.recertificarEquipo(id, idEstado, venc, cert, obs, nombreArchivo);
	        
	        ra.addFlashAttribute("msgSuccess", "Estado actualizado y certificado guardado correctamente.");
	    } catch (Exception e) {
	        ra.addFlashAttribute("msgError", "Error: " + e.getMessage());
	    }
	    
	    return "redirect:/equipo/controlEstados";
	}
	
	
	//Panel de notificaciones 
	@ModelAttribute
	public void agregarNotificaciones(Model model) {
	    List<Equipo> todos = equipoRepository.findAll();
	    LocalDate hoy = LocalDate.now();

	    List<Equipo> alertas = todos.stream()
	        .filter(e -> e.getFechaVencimiento() != null)
	        .filter(e -> java.time.temporal.ChronoUnit.DAYS.between(hoy, e.getFechaVencimiento()) <= 15)
	        .collect(java.util.stream.Collectors.toList());

	    boolean hayVencidos = alertas.stream()
	        .anyMatch(e -> e.getFechaVencimiento().isBefore(hoy));

	    model.addAttribute("equiposAlerta", alertas);
	    model.addAttribute("totalAlertas", alertas.size());
	    model.addAttribute("hayVencidos", hayVencidos); 
	}
	
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@ModelAttribute
	public void cargarUsuario(Model model, Principal principal) {
	    if (principal != null) {
	        // Buscamos al usuario por el correo que tiene la sesión activa
	        Usuario usuario = usuarioRepository.findByCorreo(principal.getName()).orElse(null);
	        
	        if (usuario != null) {
	            // Pasamos el objeto usuario al modelo para que el HTML lo vea
	            model.addAttribute("usuarioLogueado", usuario);
	        }
	    }
	}
	
	
	
	
}
