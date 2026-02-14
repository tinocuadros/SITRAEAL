package uts.edu.java.sitraeal.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "ciudad")
public class Ciudad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ciudad")
	private Integer idCiudad;

	@Column(nullable = false, length = 100)
	private String nombre;

	@Column(nullable = false, length = 100)
	private String departamento;

	// Constructores
	public Ciudad() {
		super();
	}

	public Ciudad(Integer idCiudad, String nombre, String departamento) {
		super();
		this.idCiudad = idCiudad;
		this.nombre = nombre;
		this.departamento = departamento;
	}

	// Getters y Setters
	public Integer getIdCiudad() {
		return idCiudad;
	}

	public void setIdCiudad(Integer idCiudad) {
		this.idCiudad = idCiudad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
}
