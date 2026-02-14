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
@Table(name = "equipo")
public class Equipo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_equipo")
	private Integer idEquipo;
	
	@ManyToOne
	@JoinColumn(name = "id_categoria", nullable = false)
	private CategoriaEquipo categoria;


	@ManyToOne
	@JoinColumn(name = "id_tipo", nullable = false)
	private TipoEquipo tipo;

	@ManyToOne
	@JoinColumn(name = "id_marca", nullable = false)
	private MarcaEquipo marca;

	@ManyToOne
	@JoinColumn(name = "id_norma", nullable = false)
	private Norma norma;

	@ManyToOne
	@JoinColumn(name = "id_proveedor", nullable = false)
	private Proveedor proveedor;
	
	@Column(name = "fecha_ingreso", nullable = false)
	private LocalDateTime fechaIngreso;
	
	@Column(name = "numero_factura", nullable = false)
	private String numeroFactura;

	@ManyToOne
	@JoinColumn(name = "id_estado", nullable = false)
	private EstadoEquipo estado;

	@Column(nullable = false, length = 50)
	private String lote;

	@Column(nullable = false, length = 50, unique = true)
	private String serial;

	@Column(name = "fecha_fabricacion", nullable = false)
	private LocalDate fechaFabricacion;

	@Column(name = "fecha_certificacion")
	private LocalDate fechaCertificacion;

	@Column(name = "fecha_vencimiento", nullable = false)
	private LocalDate fechaVencimiento;


	
	@Column(name = "observaciones", length = 255)
	private String observaciones;


	public Equipo() {
		
	}


	public Equipo(Integer idEquipo, CategoriaEquipo categoria, TipoEquipo tipo, MarcaEquipo marca, Norma norma,
			Proveedor proveedor, LocalDateTime fechaIngreso, String numeroFactura, EstadoEquipo estado, String lote,
			String serial, LocalDate fechaFabricacion, LocalDate fechaCertificacion, LocalDate fechaVencimiento,
			 String observaciones) {
		super();
		this.idEquipo = idEquipo;
		this.categoria = categoria;
		this.tipo = tipo;
		this.marca = marca;
		this.norma = norma;
		this.proveedor = proveedor;
		this.fechaIngreso = fechaIngreso;
		this.numeroFactura = numeroFactura;
		this.estado = estado;
		this.lote = lote;
		this.serial = serial;
		this.fechaFabricacion = fechaFabricacion;
		this.fechaCertificacion = fechaCertificacion;
		this.fechaVencimiento = fechaVencimiento;
		this.observaciones = observaciones;
	}


	public Integer getIdEquipo() {
		return idEquipo;
	}


	public void setIdEquipo(Integer idEquipo) {
		this.idEquipo = idEquipo;
	}


	public CategoriaEquipo getCategoria() {
		return categoria;
	}


	public void setCategoria(CategoriaEquipo categoria) {
		this.categoria = categoria;
	}


	public TipoEquipo getTipo() {
		return tipo;
	}


	public void setTipo(TipoEquipo tipo) {
		this.tipo = tipo;
	}


	public MarcaEquipo getMarca() {
		return marca;
	}


	public void setMarca(MarcaEquipo marca) {
		this.marca = marca;
	}


	public Norma getNorma() {
		return norma;
	}


	public void setNorma(Norma norma) {
		this.norma = norma;
	}


	public Proveedor getProveedor() {
		return proveedor;
	}


	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}


	public LocalDateTime getFechaIngreso() {
		return fechaIngreso;
	}


	public void setFechaIngreso(LocalDateTime fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}


	public String getNumeroFactura() {
		return numeroFactura;
	}


	public void setNumeroFactura(String  numeroFactura) {
		this.numeroFactura = numeroFactura;
	}


	public EstadoEquipo getEstado() {
		return estado;
	}


	public void setEstado(EstadoEquipo estado) {
		this.estado = estado;
	}


	public String getLote() {
		return lote;
	}


	public void setLote(String lote) {
		this.lote = lote;
	}


	public String getSerial() {
		return serial;
	}


	public void setSerial(String serial) {
		this.serial = serial;
	}


	public LocalDate getFechaFabricacion() {
		return fechaFabricacion;
	}


	public void setFechaFabricacion(LocalDate fechaFabricacion) {
		this.fechaFabricacion = fechaFabricacion;
	}


	public LocalDate getFechaCertificacion() {
		return fechaCertificacion;
	}


	public void setFechaCertificacion(LocalDate fechaCertificacion) {
		this.fechaCertificacion = fechaCertificacion;
	}


	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}


	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}



	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	

	


	
	
}
