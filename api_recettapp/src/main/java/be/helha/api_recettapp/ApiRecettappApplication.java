package be.helha.api_recettapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * Main class of Recipe App application
 */
@SpringBootApplication
public class ApiRecettappApplication {

	/**
	 * Main method of the app
	 * @param args arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApiRecettappApplication.class, args);
	}

}
