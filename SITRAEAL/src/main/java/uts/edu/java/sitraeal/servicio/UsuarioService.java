package uts.edu.java.sitraeal.servicio;

import java.util.List;


import uts.edu.java.sitraeal.modelo.Usuario;

public interface UsuarioService {
	
	List<Usuario> listar();
	    Usuario obtenerPorId(Integer idUsuario);
	    void guardar(Usuario usuario);
	    void eliminar(Integer idUsuario);

	 // --- NUEVOS MÉTODOS PARA RECUPERACIÓN DE CONTRASEÑA ---
	    
	   
	    String actualizarTokenContrasena(String correo);
	    Usuario obtenerPorToken(String token);
	    void actualizarContrasena(Usuario usuario, String nuevaContrasena);
}
