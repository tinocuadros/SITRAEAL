package uts.edu.java.sitraeal.servicio;

import java.util.List;


import uts.edu.java.sitraeal.modelo.Usuario;

public interface UsuarioService {
	
	List<Usuario> listar();
	    Usuario obtenerPorId(Integer idUsuario);
	    void guardar(Usuario usuario);
	    void eliminar(Integer idUsuario);

}
