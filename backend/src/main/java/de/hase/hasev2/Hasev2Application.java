package de.hase.hasev2;

import de.hase.hasev2.config.security.KeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(KeyProperties.class)
@SpringBootApplication
public class Hasev2Application {

	public static void main(String[] args) {

		SpringApplication.run(Hasev2Application.class, args);


	}

}
