package uts.edu.java.sitraeal.servicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    
    
    //Recuperar contrsaña
    @Override
    public String actualizarTokenContrasena(String correo) {
        // Buscamos al usuario por correo (Asegúrate de tener este método en el Repository)
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("No se encontró usuario con el correo: " + correo));

        // Generamos un token aleatorio único
        String token = UUID.randomUUID().toString();
        
        usuario.setResetPasswordToken(token);
        usuario.setTokenExpiration(LocalDateTime.now().plusMinutes(30)); // 30 minutos de validez
        
        usuarioRepository.save(usuario);
        return token;
    }

    @Override
    public Usuario obtenerPorToken(String token) {
        return usuarioRepository.findByResetPasswordToken(token)
                .filter(u -> u.getTokenExpiration() != null && u.getTokenExpiration().isAfter(LocalDateTime.now()))
                .orElse(null);
    }

    @Override
    public void actualizarContrasena(Usuario usuario, String nuevaContrasena) {
        // Encriptamos la nueva contraseña antes de guardar
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        
        // Limpiamos los campos de seguridad para que el link no se use otra vez
        usuario.setResetPasswordToken(null);
        usuario.setTokenExpiration(null);
        
        usuarioRepository.save(usuario);
    }
    
    
}

