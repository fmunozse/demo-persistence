package cloud.fmunozse.demopersistence;

import cloud.fmunozse.demopersistence.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class DemoPersistenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoPersistenceApplication.class, args);
	}


	//@Autowired
	//UserService userService;

	@Bean
	public CommandLineRunner loadData() {
		return (args) -> {
			//userService.insertMassiveEntityManagerWithPartialCommits();
			//userService.insertMassive();
			//userService.insertMassiveEntityManagerWithoutPartialCommits();
		};
	}

}
