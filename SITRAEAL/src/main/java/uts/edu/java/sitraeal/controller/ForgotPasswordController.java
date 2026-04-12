package uts.edu.java.sitraeal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import uts.edu.java.sitraeal.modelo.Usuario;
import uts.edu.java.sitraeal.servicio.UsuarioService;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JavaMailSender mailSender;

    // Inyectamos la URL base desde application.properties
    // Si no existe, por defecto usa la de Render para evitar errores
    @Value("${app.url:https://sitraeal.onrender.com}")
    private String appUrl;

    // A. Mostrar formulario para ingresar el correo
    @GetMapping("/forgot_password")
    public String mostrarFormularioOlvido() {
        return "views/usuario/forgot_password_form";
    }

    // B. Procesar el envío del correo
    @PostMapping("/forgot_password")
    public String procesarEnvioToken(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            String token = usuarioService.actualizarTokenContrasena(email);
            
            // CONSTRUCCIÓN DEL LINK: Usamos appUrl en lugar de HttpServletRequest
            // Esto garantiza que el link siempre sea HTTPS y apunte a Render
            String link = appUrl + "/reset_password?token=" + token;

            enviarEmail(email, link);
            redirectAttributes.addFlashAttribute("message", "Se ha enviado un enlace de recuperación a su correo.");
            
        } catch (Exception e) {
            e.printStackTrace(); // Vital para ver el error real en los Logs de Render
            redirectAttributes.addFlashAttribute("error", "No se encontró un usuario con ese correo o hubo un problema con el servicio.");
        }
        return "redirect:/login";
    }

    // C. Mostrar formulario para nueva contraseña (Validando el token)
    @GetMapping("/reset_password")
    public String mostrarFormularioReset(@RequestParam("token") String token, Model model) {
        Usuario usuario = usuarioService.obtenerPorToken(token);
        if (usuario == null) {
            model.addAttribute("error", "El enlace es inválido o ha expirado.");
            // Corregida la ruta para mantener consistencia con tus vistas
            return "views/usuario/forgot_password_form";
        }
        model.addAttribute("token", token);
        return "views/usuario/reset_password_form";
    }

    // D. Guardar la nueva contraseña
    @PostMapping("/reset_password")
    public String procesarNuevaClave(@RequestParam("token") String token, @RequestParam("password") String password, Model model) {
        Usuario usuario = usuarioService.obtenerPorToken(token);
        if (usuario == null) {
            model.addAttribute("error", "Token inválido.");
            return "views/usuario/forgot_password_form";
        }

        usuarioService.actualizarContrasena(usuario, password);
        return "redirect:/login?reset_success";
    }

    private void enviarEmail(String correoDestino, String link) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setTo(correoDestino);
        helper.setSubject("SITRAEAL - Recuperación de Acceso");

        String contenido = "<div style='font-family: Arial, sans-serif; border: 1px solid #ddd; padding: 20px;'>"
                         + "<h2 style='color: #28a745;'>SITRAEAL</h2>"
                         + "<p>Has solicitado restablecer tu contraseña.</p>"
                         + "<p>Haz clic en el botón de abajo. Este enlace vence en 30 minutos:</p>"
                         + "<div style='text-align: center; margin: 30px 0;'>"
                         + "<a href='" + link + "' style='background-color: #28a745; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold;'>Cambiar Contraseña</a>"
                         + "</div>"
                         + "<p style='margin-top: 20px; font-size: 0.8em; color: #666;'>Si no solicitaste este cambio, ignora este mensaje.</p>"
                         + "</div>";
        
        helper.setText(contenido, true);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error enviando email: " + e.getMessage());
            throw e; // Lanza el error para que lo atrape el catch de procesarEnvioToken
        }
    }
}