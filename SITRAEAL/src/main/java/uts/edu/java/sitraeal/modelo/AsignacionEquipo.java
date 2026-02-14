package uts.edu.java.sitraeal.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="asignacion_equipo")
public class AsignacionEquipo {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_asignacion")
	private Integer idAsignacion ;
	
	@ManyToOne
	@JoinColumn(name = "id_equipo", nullable = false)
	private Equipo equipo;
	
	@ManyToOne
	@JoinColumn(name="id_empleado")
	private Empleado empleado;
	
	
	@Column(name="estado_asignacion")
	private String  estadoAsignacion;
	
	@Column(name = "fecha_entrega", nullable = false)
	private LocalDateTime fechaEntrega;
	
	@Column(name = "fecha_devolucion", nullable = true)
	private LocalDateTime fechaDevolucion;
	
	

	public AsignacionEquipo() {
		
	}



	public AsignacionEquipo(Integer idAsignacion, Equipo equipo, Empleado empleado, String estadoAsignacion,
			LocalDateTime fechaEntrega, LocalDateTime fechaDevolucion) {
		super();
		this.idAsignacion = idAsignacion;
		this.equipo = equipo;
		this.empleado = empleado;
		this.estadoAsignacion = estadoAsignacion;
		this.fechaEntrega = fechaEntrega;
		this.fechaDevolucion = fechaDevolucion;
	}



	public Integer getIdAsignacion() {
		return idAsignacion;
	}



	public void setIdAsignacion(Integer idAsignacion) {
		this.idAsignacion = idAsignacion;
	}



	public Equipo getEquipo() {
		return equipo;
	}



	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}



	public Empleado getEmpleado() {
		return empleado;
	}



	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}



	public String getEstadoAsignacion() {
		return estadoAsignacion;
	}



	public void setEstadoAsignacion(String estadoAsignacion) {
		this.estadoAsignacion = estadoAsignacion;
	}



	public LocalDateTime getFechaEntrega() {
		return fechaEntrega;
	}



	public void setFechaEntrega(LocalDateTime fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}



	public LocalDateTime getFechaDevolucion() {
		return fechaDevolucion;
	}



	public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}































































	
	
}
