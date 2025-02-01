package it.unina.dietiestates25;

import it.unina.dietiestates25.auth.port.out.UserRepository;
import it.unina.dietiestates25.manager.port.out.ManagerRepository;
import it.unina.dietiestates25.model.Agency;
import it.unina.dietiestates25.model.Manager;
import it.unina.dietiestates25.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class Dietiestates25Application {

	public static void main(String[] args) {
		SpringApplication.run(Dietiestates25Application.class, args);
	}

}
