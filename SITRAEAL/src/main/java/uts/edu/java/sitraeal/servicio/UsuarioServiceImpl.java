package uts.edu.java.sitraeal.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uts.edu.java.sitraeal.modelo.Usuario;
import uts.edu.java.sitraeal.repositorio.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario obtenerPorId(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElse(null);
    }

    @Override
    public void guardar(Usuario usuario) {

        //  VALIDAR SOLO SI ES NUEVO Y TRAE ID
        if (usuario.getIdUsuario() != null &&
            usuarioRepository.existsById(usuario.getIdUsuario())) {

            throw new RuntimeException("USUARIO_EXISTE");
        }

        //  Encriptar contraseña si no lo está
        if (usuario.getContrasena() != null &&
            !usuario.getContrasena().startsWith("$2a$")) {

            usuario.setContrasena(
                passwordEncoder.encode(usuario.getContrasena())
            );
        }

        usuarioRepository.save(usuario); // ✅ AHORA SÍ INSERTA
    }


    @Override
    public void eliminar(Integer idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }
}

