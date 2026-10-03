package es.upm.sos.comunidadvinos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ComunidadvinosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComunidadvinosApplication.class, args);
	}

}
