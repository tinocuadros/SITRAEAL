package uts.edu.java.sitraeal.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "norma")
public class Norma {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_norma")
	private Integer idNorma;

	@Column(nullable = false, length = 80)
	private String nombre;

	@Column(nullable = false, length = 200)
	private String descripcion;

	// Constructor
	public Norma() {

	}

	public Norma(Integer idNorma, String nombre, String descripcion) {
		super();
		this.idNorma = idNorma;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

	// Getters y Setters
	public Integer getIdNorma() {
		return idNorma;
	}

	public void setIdNorma(Integer idNorma) {
		this.idNorma = idNorma;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
