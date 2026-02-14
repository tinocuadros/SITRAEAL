package uts.edu.java.sitraeal.repositorio;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uts.edu.java.sitraeal.modelo.AsignacionEquipo;

public interface AsignacionEquipoRepository extends JpaRepository<AsignacionEquipo, Integer> {
    List<AsignacionEquipo> findByEstadoAsignacion(String estado);
    
    boolean existsByEquipoIdEquipoAndEstadoAsignacion(Integer idEquipo, String estadoAsignacion);
    
    List<AsignacionEquipo> findByEquipoIdEquipoOrderByFechaEntregaDesc(Integer idEquipo);
    
    List<AsignacionEquipo> findByEquipoIdEquipoAndFechaEntregaBetweenOrderByFechaEntregaDesc(
            Integer idEquipo, LocalDateTime inicio, LocalDateTime fin);
}
