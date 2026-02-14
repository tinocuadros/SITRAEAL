package uts.edu.java.sitraeal.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import uts.edu.java.sitraeal.modelo.Usuario; 

public class CustomUserDetails extends User {

    
    private final Usuario usuario;

    public CustomUserDetails(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            Usuario usuario) { 

        super(username, password, authorities);
        this.usuario = usuario;
    }

   
    public Usuario getUsuario() {
        return usuario;
    }
}