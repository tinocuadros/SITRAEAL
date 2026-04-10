package uts.edu.java.sitraeal.servicio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uts.edu.java.sitraeal.modelo.Equipo;
import uts.edu.java.sitraeal.modelo.HistorialEstados;
import uts.edu.java.sitraeal.repositorio.EquipoRepository;
import uts.edu.java.sitraeal.repositorio.EstadoEquipoRepository;
import uts.edu.java.sitraeal.repositorio.HistorialEstadoRepository;

@Service
public class EquipoServiceRegis implements EquipoService {

	@Autowired
	private EquipoRepository equipoRepository;

	@Autowired
	private HistorialEstadoRepository historialRepos;

	@Autowired
	private EstadoEquipoRepository estadoRepo;

	// Listar
	@Override
	public List<Equipo> listar() {

		return equipoRepository.findAll();
	}

	// Obtener por id
	@Override
	public Equipo obtEquipoId(Integer idEquipo) {

		return equipoRepository.findById(idEquipo).orElse(null);
	}

//eliminar 
	@Override
	@Transactional
	public void eliminar(Integer idEquipo) {
		equipoRepository.deleteById(idEquipo);
	}

	
//Guardar
	@Transactional
	@Override
	public Equipo guardar(Equipo equipo) {
	    // 1. Buscar si ya existe un equipo con ese serial
		Equipo equipoExistente = equipoRepository.findBySerial(equipo.getSerial()).orElse(null);

	    if (equipoExistente != null) {
	        // ESCENARIO DE ERROR: 
	        // Si el equipo que encontré tiene un ID diferente al que estoy guardando,
	        // entonces SÍ es un duplicado de otra persona.
	        if (equipo.getIdEquipo() == null || !equipo.getIdEquipo().equals(equipoExistente.getIdEquipo())) {
	            throw new RuntimeException("Ya existe un equipo con el serial: " + equipo.getSerial());
	        }
	    }

	    // 2. Si el ID es nulo, es un equipo NUEVO (Registro inicial)
	    if (equipo.getIdEquipo() == null) {
	        if (equipo.getFechaIngreso() == null) {
	            equipo.setFechaIngreso(LocalDateTime.now());
	        }
	        
	        Equipo equipoGuardado = equipoRepository.save(equipo);

	        // Registrar historial inicial
	        HistorialEstados historial = new HistorialEstados();
	        historial.setEquipo(equipoGuardado);
	        historial.setEstadoAnterior("SISTEMA");
	        historial.setEstadoNuevo(equipoGuardado.getEstado() != null ? equipoGuardado.getEstado().getNombre() : "REGISTRADO");
	        historial.setFechaMovimiento(LocalDateTime.now());
	        historial.setObservaciones("Ingreso inicial del equipo al sistema SITRAEAL.");
	        historialRepos.save(historial);

	        return equipoGuardado;
	    } else {
	        // 3. Si el ID existe, es una EDICIÓN
	        // Aquí puedes agregar lógica específica para edición si lo deseas
	        return equipoRepository.save(equipo);
	    }
	}
	
	
	// Edictar
	@Override
	public Equipo editar(Integer idEquipo, Equipo equipo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public void recertificarEquipo(Integer idEquipo, Integer idEstado, LocalDate nuevaVenc, LocalDate nuevaCert,
			String obs, String certificacion) {
		// 1. Buscamos el equipo
		Equipo equipo = equipoRepository.findById(idEquipo)
				.orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

		// 2. Buscamos el objeto del nuevo estado
		uts.edu.java.sitraeal.modelo.EstadoEquipo nuevoEstado = estadoRepo.findById(idEstado)
				.orElseThrow(() -> new RuntimeException("Estado no encontrado"));

		// 3. REGISTRO DE HISTORIAL
		HistorialEstados historial = new HistorialEstados();
		historial.setEquipo(equipo);
		historial.setEstadoAnterior(equipo.getEstado().getNombre());
		historial.setEstadoNuevo(nuevoEstado.getNombre());

		historial.setFechaVencimientoAnterior(equipo.getFechaVencimiento());
		historial.setFechaVencimientoNueva(nuevaVenc != null ? nuevaVenc : equipo.getFechaVencimiento());
		historial.setFechaMovimiento(LocalDateTime.now());
		historial.setObservaciones(obs);

		//  NUEVO: Guardamos el nombre del archivo en el historial
		historial.setCertificacion(certificacion);

		historialRepos.save(historial);

		// 4. ACTUALIZACIÓN DEL EQUIPO
		equipo.setEstado(nuevoEstado);

		if (nuevaVenc != null)
			equipo.setFechaVencimiento(nuevaVenc);
		if (nuevaCert != null)
			equipo.setFechaCertificacion(nuevaCert);

		// 5. Guardar cambios en el equipo
		equipoRepository.saveAndFlush(equipo);
	}

	@Override
	public Equipo buscarPorSerial(String serial) {
	  
	    return equipoRepository.findBySerial(serial).orElse(null);
	}

	@Transactional
	@Scheduled(cron = "0 0 0 * * ?")
	public void actualizarEquiposVencidosAutomaticamente() {
		LocalDate hoy = LocalDate.now();

		// 1. Obtenemos el estado directamente (como lo devuelve tu repo actual)
		uts.edu.java.sitraeal.modelo.EstadoEquipo estadoVencido = estadoRepo.findByNombre("VENCIDO");

		// 2. Verificamos si existe con un IF normal
		if (estadoVencido == null) {
			System.out.println("SITRAEAL ERROR: No se encontró el estado 'VENCIDO' en la base de datos.");
			return; // Salimos para evitar el error
		}

		// 3. Buscamos equipos que vencieron
		List<Equipo> equiposPorVencer = equipoRepository.findAll().stream().filter(e -> e.getFechaVencimiento() != null)
				.filter(e -> e.getFechaVencimiento().isBefore(hoy) || e.getFechaVencimiento().isEqual(hoy))
				.filter(e -> e.getEstado() != null && !e.getEstado().getNombre().equals("VENCIDO"))
				.collect(Collectors.toList());

		for (Equipo equipo : equiposPorVencer) {
			// Registro de historial
			HistorialEstados historial = new HistorialEstados();
			historial.setEquipo(equipo);
			historial.setEstadoAnterior(equipo.getEstado().getNombre());
			historial.setEstadoNuevo("VENCIDO (AUTO)");
			historial.setFechaMovimiento(LocalDateTime.now());
			historial.setFechaVencimientoNueva(equipo.getFechaVencimiento());
			historial.setObservaciones(
					"Cambio automático realizado por el sistema SITRAEAL al cumplirse la fecha de vencimiento.");
			historialRepos.save(historial);

			// Actualización del equipo
			equipo.setEstado(estadoVencido);
			equipoRepository.save(equipo);
		}

		if (!equiposPorVencer.isEmpty()) {
			System.out
					.println("SITRAEAL INFO: Se han vencido " + equiposPorVencer.size() + " equipos automáticamente.");
		}
	}

}
