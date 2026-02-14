package uts.edu.java.sitraeal.servicio;

import java.util.List;

import org.springframework.stereotype.Service;
import uts.edu.java.sitraeal.modelo.Empleado;
import uts.edu.java.sitraeal.repositorio.EmpleadoRepository;

@Service
public class EmpleadoServiceEmp implements EmpleadoServicie {

	
	private final EmpleadoRepository empleadoRepository;

	EmpleadoServiceEmp(EmpleadoRepository empleadoRepository) {
		this.empleadoRepository = empleadoRepository;
	}

	@Override
	public List<Empleado> listar() {
		
		return empleadoRepository.findAll();
	}

	@Override
	public Empleado obtEmpleadoId(Integer idEmpleado) {
		// TODO Auto-generated method stub
		return empleadoRepository.findById(idEmpleado).orElse(null);
	}

	@Override
	public void guardar(Empleado empleado) {
		// Valida que el empleado exista
		if (empleado.getIdEmpleado() != null && empleadoRepository.existsById(empleado.getIdEmpleado())) {

			throw new RuntimeException("EMPLEADO_EXISTE");
		}
		empleadoRepository.save(empleado);

	}
	

	@Override
	public void eliminar(Integer idEmpleado) {
		// TODO Auto-generated method stub

	}

	@Override
	public Empleado editar(Integer idEmpleado, Empleado empleadoNuevo) {
		Empleado empleado = empleadoRepository.findById(idEmpleado)
				.orElseThrow(() -> new RuntimeException("EMPLEADO_NO_EXISTE"));

		empleado.setNombres(empleadoNuevo.getNombres());
		empleado.setApellidos(empleadoNuevo.getApellidos());
		empleado.setCorreo(empleadoNuevo.getCorreo());
		empleado.setCiudad(empleadoNuevo.getCiudad());
		empleado.setDireccion(empleadoNuevo.getDireccion());
		empleado.setFechaNacimiento(empleadoNuevo.getFechaNacimiento());
		

		return empleadoRepository.save(empleado);
	}

}
