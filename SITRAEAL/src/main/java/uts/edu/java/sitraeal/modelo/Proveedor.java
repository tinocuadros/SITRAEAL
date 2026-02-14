package uts.edu.java.sitraeal.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "proveedor")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 100, unique = true)
    private String correo;

    @ManyToOne
    @JoinColumn(name = "id_ciudad", nullable = false)
    private Ciudad ciudad;

    @Column(nullable = false, length = 150)
    private String direccion;

    // Constructores
    
    public Proveedor() {
    	
    }
    
    

    public Proveedor(Integer idProveedor, String nombre, String telefono, String correo, Ciudad ciudad,
			String direccion) {
		super();
		this.idProveedor = idProveedor;
		this.nombre = nombre;
		this.telefono = telefono;
		this.correo = correo;
		this.ciudad = ciudad;
		this.direccion = direccion;
	}

 // Getters and Setters

	public Integer getIdProveedor() {
		return idProveedor;
	}



	public void setIdProveedor(Integer idProveedor) {
		this.idProveedor = idProveedor;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getTelefono() {
		return telefono;
	}



	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}



	public String getCorreo() {
		return correo;
	}



	public void setCorreo(String correo) {
		this.correo = correo;
	}



	public Ciudad getCiudad() {
		return ciudad;
	}



	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}



	public String getDireccion() {
		return direccion;
	}



	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}



	

}
