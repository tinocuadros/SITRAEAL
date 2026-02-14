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
@Table(name = "reporte_incidente")
public class ReporteIncidente {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Integer idReporte;

    @Column(nullable = false, length = 50)
    private String tipo; // ACCIDENTE, INCIDENTE, FALLA_TECNICA

    @Column(name = "fecha_suceso", nullable = false)
    private LocalDateTime fechaSuceso;

    @Column(nullable = false, length = 20)
    private String gravedad; // BAJA, MEDIA, ALTA

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "acciones_tomadas", length = 255)
    private String accionesTomadas;

   
    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;


	public ReporteIncidente() {
		
	}


	public ReporteIncidente(Integer idReporte, String tipo, LocalDateTime fechaSuceso, String gravedad,
			String descripcion, String accionesTomadas, Equipo equipo, Empleado empleado) {
		super();
		this.idReporte = idReporte;
		this.tipo = tipo;
		this.fechaSuceso = fechaSuceso;
		this.gravedad = gravedad;
		this.descripcion = descripcion;
		this.accionesTomadas = accionesTomadas;
		this.equipo = equipo;
		this.empleado = empleado;
	}


	public Integer getIdReporte() {
		return idReporte;
	}


	public void setIdReporte(Integer idReporte) {
		this.idReporte = idReporte;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public LocalDateTime getFechaSuceso() {
		return fechaSuceso;
	}


	public void setFechaSuceso(LocalDateTime fechaSuceso) {
		this.fechaSuceso = fechaSuceso;
	}


	public String getGravedad() {
		return gravedad;
	}


	public void setGravedad(String gravedad) {
		this.gravedad = gravedad;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getAccionesTomadas() {
		return accionesTomadas;
	}


	public void setAccionesTomadas(String accionesTomadas) {
		this.accionesTomadas = accionesTomadas;
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
	
	
}
