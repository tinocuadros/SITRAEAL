package uts.edu.java.sitraeal.servicio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import uts.edu.java.sitraeal.modelo.Usuario;
import uts.edu.java.sitraeal.repositorio.UsuarioRepository;
import uts.edu.java.sitraeal.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("No encontrado"));

        String rol = (usuario.getRol() != null) ? usuario.getRol().getNombre() : "USER";
        if (!rol.startsWith("ROLE_")) rol = "ROLE_" + rol;

        // Definimos la lista con el tipo exacto que pide el superconstructor
        List<GrantedAuthority> autoridades = new ArrayList<>();
        autoridades.add(new SimpleGrantedAuthority(rol));

        return new CustomUserDetails(
                usuario.getCorreo(),
                usuario.getContrasena(),
                autoridades, 
                usuario
        );
    }
}
