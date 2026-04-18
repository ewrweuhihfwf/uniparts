package com.example.parts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PartsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartsApplication.class, args);
	}

}
