package uts.edu.java.sitraeal.modelo;

import java.time.LocalDate;
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
@Table(name = "historial_estados")
public class HistorialEstados {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Integer idHistorial;

    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    @Column(name = "estado_anterior", length = 50)
    private String estadoAnterior;

    @Column(name = "estado_nuevo", length = 50)
    private String estadoNuevo;

    @Column(name = "fecha_vencimiento_anterior")
    private LocalDate fechaVencimientoAnterior;

    @Column(name = "fecha_vencimiento_nueva")
    private LocalDate fechaVencimientoNueva;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;
    
    @Column(name="certificacion")
    private  String certificacion;

    @Column(length = 500)
    private String observaciones;

	public HistorialEstados() {
		
	}

	public HistorialEstados(Integer idHistorial, Equipo equipo, String estadoAnterior, String estadoNuevo,
			LocalDate fechaVencimientoAnterior, LocalDate fechaVencimientoNueva, LocalDateTime fechaMovimiento,
			String certificacion, String observaciones) {
		super();
		this.idHistorial = idHistorial;
		this.equipo = equipo;
		this.estadoAnterior = estadoAnterior;
		this.estadoNuevo = estadoNuevo;
		this.fechaVencimientoAnterior = fechaVencimientoAnterior;
		this.fechaVencimientoNueva = fechaVencimientoNueva;
		this.fechaMovimiento = fechaMovimiento;
		this.certificacion = certificacion;
		this.observaciones = observaciones;
	}

	public Integer getIdHistorial() {
		return idHistorial;
	}

	public void setIdHistorial(Integer idHistorial) {
		this.idHistorial = idHistorial;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	public String getEstadoAnterior() {
		return estadoAnterior;
	}

	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}

	public String getEstadoNuevo() {
		return estadoNuevo;
	}

	public void setEstadoNuevo(String estadoNuevo) {
		this.estadoNuevo = estadoNuevo;
	}

	public LocalDate getFechaVencimientoAnterior() {
		return fechaVencimientoAnterior;
	}

	public void setFechaVencimientoAnterior(LocalDate fechaVencimientoAnterior) {
		this.fechaVencimientoAnterior = fechaVencimientoAnterior;
	}

	public LocalDate getFechaVencimientoNueva() {
		return fechaVencimientoNueva;
	}

	public void setFechaVencimientoNueva(LocalDate fechaVencimientoNueva) {
		this.fechaVencimientoNueva = fechaVencimientoNueva;
	}

	public LocalDateTime getFechaMovimiento() {
		return fechaMovimiento;
	}

	public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
	}

	public String getCertificacion() {
		return certificacion;
	}

	public void setCertificacion(String certificacion) {
		this.certificacion = certificacion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	
}
