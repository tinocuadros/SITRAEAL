package uts.edu.java.sitraeal.repositorio;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uts.edu.java.sitraeal.modelo.Equipo;
import uts.edu.java.sitraeal.modelo.HistorialEstados;

public interface HistorialEstadoRepository extends JpaRepository<HistorialEstados, Integer> {
	
	List<HistorialEstados> findByEquipoIdEquipoOrderByFechaMovimientoDesc(Integer idEquipo);
	List<HistorialEstados> findByEquipoOrderByFechaMovimientoDesc(Equipo equipo);
	List<HistorialEstados> findByEquipoIdEquipoAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
		    Integer idEquipo, LocalDateTime inicio, LocalDateTime fin);
}
