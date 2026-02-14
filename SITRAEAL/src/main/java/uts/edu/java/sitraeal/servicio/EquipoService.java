package uts.edu.java.sitraeal.servicio;

import java.time.LocalDate;
import java.util.List;

import uts.edu.java.sitraeal.modelo.Equipo;

public interface EquipoService {
	List<Equipo>listar();
	Equipo obtEquipoId(Integer idEquipo);
	Equipo guardar(Equipo equipo);
	void eliminar(Integer idEquipo);
	Equipo editar(Integer idEquipo, Equipo equipo);
Equipo buscarPorSerial(String serial);
    
//En EquipoService.java
void recertificarEquipo(Integer idEquipo, Integer idEstado, LocalDate nuevaVenc, LocalDate nuevaCert, String obs);
}
