package uts.edu.java.sitraeal.servicio;

import java.util.List;

import org.springframework.stereotype.Service;

import uts.edu.java.sitraeal.modelo.HistorialEstados;

public interface HistorialEstadoService {
	    // Método para obtener todos los cambios de un equipo por su ID
	    List<HistorialEstados> listarHistorialPorEquipo(Integer idEquipo);
	    
	    // Método 
	    void guardar(HistorialEstados historial);
}
