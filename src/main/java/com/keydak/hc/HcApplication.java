package com.keydak.hc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HcApplication {

	public static void main(String[] args) {
		SpringApplication.run(HcApplication.class, args);
	}

}
