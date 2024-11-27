package com.firstspringtutorial.dreamshops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableJpaRepositories
@SpringBootApplication
public class DreamShopsApplication { 

	public static void main(String[] args) {
		SpringApplication.run(DreamShopsApplication.class, args);
	}

}
