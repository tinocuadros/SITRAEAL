package uts.edu.java.sitraeal.config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    // Apuntamos a la carpeta 'uploads' en la raíz
	    String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
	    
	    if (!resourcePath.endsWith("/")) {
	        resourcePath += "/";
	    }

	    registry.addResourceHandler("/uploads/**")
	            .addResourceLocations(resourcePath);
	            
	    System.out.println("SITRAEAL: Archivos servidos desde: " + resourcePath);
	}
}