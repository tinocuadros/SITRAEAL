package uts.edu.java.sitraeal.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uts.edu.java.sitraeal.modelo.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreo(String correo);
    boolean existsById(Integer idUsuario); // valida que el usuario no exita en la BD
   
}

