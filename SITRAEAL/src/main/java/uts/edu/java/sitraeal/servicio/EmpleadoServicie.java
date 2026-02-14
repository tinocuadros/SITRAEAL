package uts.edu.java.sitraeal.servicio;

import java.util.List;

import uts.edu.java.sitraeal.modelo.Empleado;

public interface EmpleadoServicie {
List<Empleado>listar();
Empleado obtEmpleadoId(Integer idEmpleado);
void guardar(Empleado empleado);
void eliminar(Integer idEmpleado);
Empleado editar(Integer idEmpleado, Empleado empleado);


}
