package uts.edu.java.sitraeal.servicio;

import java.util.List;

import uts.edu.java.sitraeal.modelo.AsignacionEquipo;

public interface AsignacionEquipoService {
    List<AsignacionEquipo> listar();
    AsignacionEquipo obtAsignacionId(Integer idAsignacion); 
    void guardar(AsignacionEquipo asignacionEquipo);
    void eliminar(Integer idAsignacion);
    AsignacionEquipo editar(Integer idAsignacion, AsignacionEquipo asignacionEquipo);
    List<AsignacionEquipo> listarSoloAsignados();
}
