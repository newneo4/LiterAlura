package alura.literalura;

import alura.literalura.principal.Principal;
import alura.literalura.repository.IAutoresDbRepository;
import alura.literalura.repository.ILibrosDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private ILibrosDbRepository Librorepository;

	@Autowired
	private IAutoresDbRepository Autorrepository;

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(Librorepository, Autorrepository);
		principal.muestraElMenu();
	}
}
