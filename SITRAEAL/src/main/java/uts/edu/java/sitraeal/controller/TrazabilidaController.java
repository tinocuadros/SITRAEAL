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

        // 1. Si no hay criterio, solo mostrar la página de búsqueda
        if (criterio == null || criterio.isEmpty()) {
            return "views/trazabilidad/trazabilidad";
        }

        // 2. VALIDACIÓN ESTRICTA: Pedir fechas si faltan
        if (fechaInicio == null || fechaFin == null) {
            model.addAttribute("error", "Por favor, seleccione el rango de fechas (Inicio y Fin) para realizar la consulta.");
            return "views/trazabilidad/trazabilidad";
        }

        // 3. Buscar el Equipo
        Optional<Equipo> equipoOpt = equipoRepository.findBySerial(criterio.trim().toUpperCase());

     // Si no se encontró por serial, intentamos por ID
        if (equipoOpt.isEmpty() && criterio.matches("\\d+")) {
            equipoOpt = equipoRepository.findById(Integer.parseInt(criterio));
        }

        if (equipoOpt.isPresent()) {
            Equipo equipo = equipoOpt.get();
            model.addAttribute("equipo", equipo);

            // 4. Convertir LocalDates a LocalDateTimes para cubrir el día completo
            LocalDateTime inicio = fechaInicio.atStartOfDay();
            LocalDateTime fin = fechaFin.atTime(23, 59, 59);

            // 5. Consultas filtradas por rango de fecha
            model.addAttribute("historialEstados",
                historialEstadoRepository.findByEquipoIdEquipoAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
                    equipo.getIdEquipo(), inicio, fin));

            model.addAttribute("historialAsignaciones",
                asignacionEquipoRepository.findByEquipoIdEquipoAndFechaEntregaBetweenOrderByFechaEntregaDesc(
                    equipo.getIdEquipo(), inicio, fin));

        } else {
            model.addAttribute("error", "No se encontró el equipo con el Serial: " + criterio);
        }

        return "views/trazabilidad/trazabilidad";
    }
}
	