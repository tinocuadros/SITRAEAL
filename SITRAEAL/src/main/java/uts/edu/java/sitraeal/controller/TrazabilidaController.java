package uts.edu.java.sitraeal.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import uts.edu.java.sitraeal.modelo.Equipo;
import uts.edu.java.sitraeal.repositorio.AsignacionEquipoRepository;
import uts.edu.java.sitraeal.repositorio.EquipoRepository;
import uts.edu.java.sitraeal.repositorio.HistorialEstadoRepository;

@Controller
public class TrazabilidaController {

	private final EquipoRepository equipoRepository;
	private final AsignacionEquipoRepository asignacionEquipoRepository;
	private final HistorialEstadoRepository historialEstadoRepository;

	public TrazabilidaController(EquipoRepository equipoRepository,
			AsignacionEquipoRepository asignacionEquipoRepository,
			HistorialEstadoRepository historialEstadoRepository) {
		this.equipoRepository = equipoRepository;
		this.asignacionEquipoRepository = asignacionEquipoRepository;
		this.historialEstadoRepository = historialEstadoRepository;
	}

	@GetMapping("/equipo/trazabilidad")
	public String verTrazabilidad(
	        @RequestParam(name = "criterio", required = false) String criterio,
	        @RequestParam(name = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
	        @RequestParam(name = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
	        Model model) {
	    
	    if (criterio == null || criterio.isEmpty())
	        return "views/trazabilidad/trazabilidad";

	    Optional<Equipo> equipoOpt = equipoRepository.findBySerial(criterio);
	    if (equipoOpt.isEmpty() && criterio.matches("\\d+")) {
	        equipoOpt = equipoRepository.findById(Integer.parseInt(criterio));
	    }

	    if (equipoOpt.isPresent()) {
	        Equipo equipo = equipoOpt.get();
	        model.addAttribute("equipo", equipo);

	        // Definimos un rango de fechas por defecto para evitar enviar NULL al repositorio
	        LocalDateTime inicio = (fechaInicio != null) ? fechaInicio.atStartOfDay() 
	                                                     : LocalDateTime.of(2000, 1, 1, 0, 0);
	        LocalDateTime fin = (fechaFin != null) ? fechaFin.atTime(23, 59, 59) 
	                                               : LocalDateTime.now().plusYears(1);

	        // Siempre usamos el método 'Between' pasando el rango (si no hay filtros, el rango es gigante)
	        model.addAttribute("historialEstados",
	            historialEstadoRepository.findByEquipoIdEquipoAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
	                equipo.getIdEquipo(), inicio, fin));
	        
	        model.addAttribute("historialAsignaciones", 
	            asignacionEquipoRepository.findByEquipoIdEquipoAndFechaEntregaBetweenOrderByFechaEntregaDesc(
	                equipo.getIdEquipo(), inicio, fin));

	    } else {
	        model.addAttribute("error", "No se encontró información para: " + criterio);
	    }

	    return "views/trazabilidad/trazabilidad";
	}}