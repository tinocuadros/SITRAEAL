package uts.edu.java.sitraeal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(
	                "/login",
	                "/usuario/nuevo",
	                "/css/**",
	                "/js/**",
	                "/img/**"
	            ).permitAll()

	            // ✅ PERMITIR POST A /usuario
	            .requestMatchers(org.springframework.http.HttpMethod.POST, "/usuario")
	            .permitAll()

	            // ✅ PERMITIR GET /usuario (listar)
	            .requestMatchers(org.springframework.http.HttpMethod.GET, "/usuario")
	            .permitAll()

	            .anyRequest().authenticated()
	        )
	        .formLogin(form -> form
	            .loginPage("/login")
	            .defaultSuccessUrl("/", true)
	            .permitAll()
	        )
	        .logout(logout -> logout
	            .logoutSuccessUrl("/login?logout")
	            .permitAll()
	        );

	    return http.build();
	}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


