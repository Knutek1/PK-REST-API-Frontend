package com.kodilla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PkRestApiFrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PkRestApiFrontendApplication.class, args);
	}

}
