package uts.edu.java.sitraeal.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uts.edu.java.sitraeal.modelo.Empleado;

@Repository
public interface EmpleadoRepository  extends JpaRepository<Empleado,Integer>{
 
}
