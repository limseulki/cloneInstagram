package com.example.cloneinstagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CloneInstagramApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloneInstagramApplication.class, args);
	}

}
