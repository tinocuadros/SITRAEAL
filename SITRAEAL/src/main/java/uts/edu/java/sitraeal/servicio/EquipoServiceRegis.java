package uts.edu.java.sitraeal.servicio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uts.edu.java.sitraeal.modelo.Equipo;
import uts.edu.java.sitraeal.modelo.HistorialEstados;
import uts.edu.java.sitraeal.repositorio.EquipoRepository;
import uts.edu.java.sitraeal.repositorio.EstadoEquipoRepository;
import uts.edu.java.sitraeal.repositorio.HistorialEstadoRepository;

@Service
public class EquipoServiceRegis  implements EquipoService {

	@Autowired
	private  EquipoRepository equipoRepository;
	 
	@Autowired
	private HistorialEstadoRepository historialRepos;
	
	@Autowired
	private EstadoEquipoRepository estadoRepo;
	
	//Listar
	@Override
	public List<Equipo> listar() {
	
		return equipoRepository.findAll();
	}

	
	//Obtener por id 
	@Override
	public Equipo obtEquipoId(Integer idEquipo) {
		
		return equipoRepository.findById(idEquipo).orElse(null);
	}

	

//eliminar 
	@Override
	public void eliminar(Integer idEquipo) {
		// TODO Auto-generated method stub
		
	}

	

	@Transactional 
	@Override
	public Equipo guardar(Equipo equipo) {
	    // 1. Validación de existencia
	    if (equipo.getIdEquipo() != null && equipoRepository.existsById(equipo.getIdEquipo())) {
	        throw new RuntimeException("EQUIPO_EXISTE");
	    }

	    // 2. Establecer fecha de ingreso automática si no viene
	    if (equipo.getFechaIngreso() == null) {
	        equipo.setFechaIngreso(LocalDateTime.now());
	    }

	    // 3. Guardar el equipo primero para obtener su ID
	    Equipo equipoGuardado = equipoRepository.save(equipo);

	    // 4. REGISTRO DE TRAZABILIDAD (Para el módulo de historial)
	    HistorialEstados historial = new HistorialEstados();
	    historial.setEquipo(equipoGuardado);
	    historial.setEstadoAnterior("SISTEMA"); // Indica que es un registro nuevo
	    
	    // Obtenemos el nombre del estado actual (ej: DISPONIBLE)
	    String nombreEstado = (equipoGuardado.getEstado() != null) 
	                          ? equipoGuardado.getEstado().getNombre() 
	                          : "REGISTRADO";
	    
	    historial.setEstadoNuevo(nombreEstado);
	    historial.setFechaMovimiento(LocalDateTime.now());
	    historial.setFechaVencimientoNueva(equipoGuardado.getFechaVencimiento());
	    historial.setObservaciones("Ingreso inicial del equipo al sistema SITRAEAL. Control de inspección activado.");
	    
	    // Guardamos en la tabla historial_estados
	    historialRepos.save(historial);

	    return equipoGuardado;
	}

	//Edictar 
	@Override
	public Equipo editar(Integer idEquipo, Equipo equipo) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Transactional
	@Override
	public void recertificarEquipo(Integer idEquipo, Integer idEstado, LocalDate nuevaVenc, LocalDate nuevaCert, String obs) {
	    // Buscamos el equipo
	    Equipo equipo = equipoRepository.findById(idEquipo)
	            .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

	    //  Buscamos el objeto del nuevo estado usando el idEstado que viene del combo
	    uts.edu.java.sitraeal.modelo.EstadoEquipo nuevoEstado = estadoRepo.findById(idEstado)
	            .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

	 // REGISTRO DE HISTORIAL
	    HistorialEstados historial = new HistorialEstados();
	    historial.setEquipo(equipo);
	    historial.setEstadoAnterior(equipo.getEstado().getNombre());
	    historial.setEstadoNuevo(nuevoEstado.getNombre());
	    
	    // Si la nueva fecha es nula, el historial registra la que ya tenía
	    historial.setFechaVencimientoAnterior(equipo.getFechaVencimiento());
	    historial.setFechaVencimientoNueva(nuevaVenc != null ? nuevaVenc : equipo.getFechaVencimiento());
	    historial.setFechaMovimiento(LocalDateTime.now());
	    historial.setObservaciones(obs);
	    historialRepos.save(historial);

	    // ACTUALIZACIÓN DEL EQUIPO
	    equipo.setEstado(nuevoEstado);
	    
	    // SOLO ACTUALIZA LA FECHA SI EL USUARIO ENVIÓ UNA NUEVA
	    if (nuevaVenc != null) equipo.setFechaVencimiento(nuevaVenc);
	    if (nuevaCert != null) equipo.setFechaCertificacion(nuevaCert);
	    
	    // 5. Guardar cambios en el equipo
	    equipoRepository.saveAndFlush(equipo); 
	}
	
	
	

	@Override
	public Equipo buscarPorSerial(String serial) {
	    // Necesitas tener findBySerial definido en tu EquipoRepository
	    return equipoRepository.findBySerial(serial).orElse(null);
	}


	

	
	

}
