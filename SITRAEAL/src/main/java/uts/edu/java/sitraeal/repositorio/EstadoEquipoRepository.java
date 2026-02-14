package uts.edu.java.sitraeal.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import uts.edu.java.sitraeal.modelo.EstadoEquipo;

public interface EstadoEquipoRepository extends JpaRepository<EstadoEquipo, Integer> {
	EstadoEquipo findByNombre(String nombre);
	Optional<EstadoEquipo> findOptionalByNombre(String nombre);

}
