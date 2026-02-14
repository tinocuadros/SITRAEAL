package uts.edu.java.sitraeal.servicio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uts.edu.java.sitraeal.modelo.AsignacionEquipo;
import uts.edu.java.sitraeal.modelo.Equipo;
import uts.edu.java.sitraeal.modelo.EstadoEquipo;
import uts.edu.java.sitraeal.repositorio.AsignacionEquipoRepository;
import uts.edu.java.sitraeal.repositorio.EquipoRepository;
import uts.edu.java.sitraeal.repositorio.EstadoEquipoRepository;
import uts.edu.java.sitraeal.repositorio.HistorialEstadoRepository;

@Service
public class AsignacionEquipoAsig implements AsignacionEquipoService {

	private final AsignacionEquipoRepository asignacionEquipoRepository;

	AsignacionEquipoAsig(AsignacionEquipoRepository asignacionEquipoRepository) {
		this.asignacionEquipoRepository = asignacionEquipoRepository;
	}
	
	@Autowired
	private HistorialEstadoRepository historialRepos; 
	
	@Autowired
	private EquipoRepository equipoRepo;
	
	@Autowired
	private EstadoEquipoRepository estadoEquipoRepository;

//listar la asignacion
	@Override
	public List<AsignacionEquipo> listar() {

		return asignacionEquipoRepository.findAll();
	}

//Obtener por id 
	@Override
	public AsignacionEquipo obtAsignacionId(Integer idAsignacion) {
		return asignacionEquipoRepository.findById(idAsignacion)
				.orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
	}

	
	//Guardar la asignacion

	@Override
	@Transactional
	public void guardar(AsignacionEquipo asignacionEquipo) {
	    // 1. Buscar el equipo
	    Equipo equipo = equipoRepo.findById(asignacionEquipo.getEquipo().getIdEquipo())
	            .orElseThrow(() -> new RuntimeException("Equipo no existe"));

	    // 2. VALIDACIÓN: ¿Está el equipo asignado actualmente a alguien?
	    // Buscamos si existe alguna asignación previa con estado "ASIGNADO" para este equipo
	    boolean yaAsignado = asignacionEquipoRepository.existsByEquipoIdEquipoAndEstadoAsignacion(
	            equipo.getIdEquipo(), "ASIGNADO");

	    if (yaAsignado) {
	        throw new RuntimeException("El equipo ya se encuentra asignado a otro trabajador.");
	    }

	    // 3. Validar Disponibilidad por Estado (Doble verificación)
	    if (!equipo.getEstado().getNombre().equals("DISPONIBLE")) {
	        throw new RuntimeException("El equipo no se puede asignar porque está en estado: " + equipo.getEstado().getNombre());
	    }

	    // 4. VALIDACIÓN DE FECHA DE VENCIMIENTO (Regla de los 15 días)
	    if (equipo.getFechaVencimiento() == null) {
	        throw new RuntimeException("El equipo no tiene una fecha de vencimiento registrada.");
	    }

	    LocalDate hoy = LocalDate.now();
	    LocalDate vencimiento = equipo.getFechaVencimiento();

	    if (vencimiento.isBefore(hoy)) {
	        throw new RuntimeException("No se puede asignar: El equipo está VENCIDO desde el " + vencimiento);
	    }

	    long diasParaVencer = ChronoUnit.DAYS.between(hoy, vencimiento);
	    if (diasParaVencer <= 15) {
	        throw new RuntimeException("No se puede asignar: El equipo vence en " + diasParaVencer + " días (Plazo mínimo de seguridad: 15 días)");
	    }

	 // 5. Guardar la Asignación
	    asignacionEquipo.setEstadoAsignacion("ASIGNADO");
	    asignacionEquipo.setFechaEntrega(LocalDateTime.now());                    
	    asignacionEquipoRepository.save(asignacionEquipo);

	    // 6. Cambiar estado del objeto Equipo
	    EstadoEquipo estadoAsignado = estadoEquipoRepository.findByNombre("ASIGNADO");
	    if (estadoAsignado == null) {
	        throw new RuntimeException("Error: El estado 'ASIGNADO' no existe.");
	    }
	    
	    // GUARDAR EN HISTORIAL ANTES DE CAMBIAR EL ESTADO REAL
	    uts.edu.java.sitraeal.modelo.HistorialEstados historial = new uts.edu.java.sitraeal.modelo.HistorialEstados();
	    historial.setEquipo(equipo);
	    historial.setEstadoAnterior(equipo.getEstado().getNombre());
	    historial.setEstadoNuevo(estadoAsignado.getNombre());
	    historial.setFechaMovimiento(LocalDateTime.now());
	    historial.setObservaciones("ASIGNACIÓN: Equipo entregado a " + 
	        asignacionEquipo.getEmpleado().getNombres() + " " + asignacionEquipo.getEmpleado().getApellidos());
	    historialRepos.save(historial);

	    // Actualizar equipo
	    equipo.setEstado(estadoAsignado);
	    equipoRepo.save(equipo);
	}

	@Override
	public void eliminar(Integer idAsignacion) {
		asignacionEquipoRepository.deleteById(idAsignacion);

	}

	
	//Edictar cambiamos de estado asignado a devuelto
	@Override
	@Transactional 
	public AsignacionEquipo editar(Integer idAsignacion, AsignacionEquipo asignacionEquipo) {
	    // Buscamos la asignación
	    AsignacionEquipo asignacion = obtAsignacionId(idAsignacion);

	    if (!"ASIGNADO".equals(asignacion.getEstadoAsignacion())) {
	        throw new RuntimeException("El equipo ya fue devuelto o no está asignado.");
	    }

	    // Datos de devolución
	    asignacion.setEstadoAsignacion("DEVUELTO");
	    asignacion.setFechaDevolucion(LocalDateTime.now());

	    Equipo equipo = asignacion.getEquipo();
	    EstadoEquipo estadoFinal;

	    // Lógica de estado según vencimiento
	    if (equipo.getFechaVencimiento() != null && equipo.getFechaVencimiento().isBefore(LocalDate.now())) {
	        estadoFinal = estadoEquipoRepository.findByNombre("VENCIDO");
	    } else {
	        estadoFinal = estadoEquipoRepository.findByNombre("DISPONIBLE");
	    }

	    if (estadoFinal == null) {
	        throw new RuntimeException("Error: El estado de retorno no existe.");
	    }

	    // REGISTRO EN HISTORIAL DE DEVOLUCIÓN
	    uts.edu.java.sitraeal.modelo.HistorialEstados historial = new uts.edu.java.sitraeal.modelo.HistorialEstados();
	    historial.setEquipo(equipo);
	    historial.setEstadoAnterior(equipo.getEstado().getNombre());
	    historial.setEstadoNuevo(estadoFinal.getNombre());
	    historial.setFechaMovimiento(LocalDateTime.now());
	    historial.setObservaciones("DEVOLUCIÓN: Equipo retornado por " + 
	        asignacion.getEmpleado().getNombres() + ". Asignación #" + idAsignacion);
	    historialRepos.save(historial);

	    // Actualizar el equipo y la asignación
	    equipo.setEstado(estadoFinal);
	    equipoRepo.save(equipo);
	    
	    return asignacionEquipoRepository.save(asignacion);
	}
	
	@Override
	public List<AsignacionEquipo> listarSoloAsignados() {
	    return asignacionEquipoRepository.findByEstadoAsignacion("ASIGNADO");
	}
	
	
}

