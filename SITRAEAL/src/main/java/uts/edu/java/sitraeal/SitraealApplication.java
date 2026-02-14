package uts.edu.java.sitraeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SitraealApplication {

	public static void main(String[] args) {
		SpringApplication.run(SitraealApplication.class, args);
	}

}
