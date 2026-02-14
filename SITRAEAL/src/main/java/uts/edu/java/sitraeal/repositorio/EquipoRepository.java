package uts.edu.java.sitraeal.repositorio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uts.edu.java.sitraeal.modelo.Equipo;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
	Optional<Equipo> findBySerial(String serial);
	List<Equipo> findByFechaVencimientoBefore(LocalDate limite);
	List<Equipo> findByFechaVencimientoBeforeAndEstado(LocalDate fecha, String estado);
}
