package uts.edu.java.sitraeal.controller;


import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

	
	//Guardar
	@PostMapping("/guardar")
	public String guardarEquipo(
	        @ModelAttribute("equipo") Equipo equipo,
	        RedirectAttributes redirectAttributes) {

	    try {
	        
	        if (equipo.getSerial() != null) {
	            equipo.setSerial(equipo.getSerial().toUpperCase().trim());
	        }

	        equipo.setFechaIngreso(LocalDateTime.now());
	        Equipo equipoGuardado = service.guardar(equipo);

	        redirectAttributes.addFlashAttribute("msgSuccess", 
	            "Equipo registrado correctamente con ID #: " + equipoGuardado.getIdEquipo() + "Con Serial #: " +equipoGuardado.getSerial());
	        
	        return "redirect:/equipo/nuevo";

	    } catch (Exception e) {
	        // 2. Detectar si el error es por el Serial Duplicado
	        String mensajeError = "Error al guardar el equipo.";
	        
	        // Buscamos palabras clave en el error de la base de datos
	        if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("constraint") || e.getMessage().contains("Unique")) {
	            mensajeError = "El Serial '" + equipo.getSerial() + "' ya existe. Por favor, verifique e intente con otro.";
	        } else {
	            mensajeError += " Detalle: " + e.getMessage();
	        }

	        e.printStackTrace(); 
	        redirectAttributes.addFlashAttribute("msgError", mensajeError);
	        return "redirect:/equipo/nuevo";
	    }
	}
	
	
	//Cambiar estados 
	@GetMapping("/controlEstados")
	public String mostrarControlEstados(
	        @org.springframework.web.bind.annotation.RequestParam(value = "criterio", required = false) String criterio, 
	        Model model) {
	    
	  
	    model.addAttribute("estados", estadoEquipoRepository.findAll());

	    if (criterio != null && !criterio.isEmpty()) {
	        Equipo equipo = null;
	        
	        try {
	            // Intentamos buscar por ID
	            Integer id = Integer.parseInt(criterio);
	            equipo = service.obtEquipoId(id); 
	        } catch (NumberFormatException e) {
	            // Si no es número, buscamos por Serial
	            equipo = service.buscarPorSerial(criterio); 
	        }

	        if (equipo != null) {
	            model.addAttribute("equipo", equipo);
	        } else {
	            model.addAttribute("error", "No se encontró equipo con ID o Serial: " + criterio);
	        }
	    }
	    
	    
	    return "views/equipo/controlEstados"; 
	}
	
	
	//Actualizar Estados
	@PostMapping("/actualizar-estado-completo")
	public String actualizarEstadoCompleto(
	        @org.springframework.web.bind.annotation.RequestParam("idEquipo") Integer id,
	        @org.springframework.web.bind.annotation.RequestParam("idEstado") Integer idEstado, // ID del combo
	        @org.springframework.web.bind.annotation.RequestParam("fechaCertificacion") String fCert,
	        @org.springframework.web.bind.annotation.RequestParam("fechaVencimiento") String fVenc,
	        @org.springframework.web.bind.annotation.RequestParam("observaciones") String obs,
	        RedirectAttributes ra) {

		try {
	        
	        LocalDate cert = (fCert != null && !fCert.isEmpty()) ? LocalDate.parse(fCert) : null;
	        LocalDate venc = (fVenc != null && !fVenc.isEmpty()) ? LocalDate.parse(fVenc) : null;

	        service.recertificarEquipo(id, idEstado, venc, cert, obs);
	        ra.addFlashAttribute("msgSuccess", "Estado actualizado correctamente.");
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
