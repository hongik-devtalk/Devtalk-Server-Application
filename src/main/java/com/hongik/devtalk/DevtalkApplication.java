package com.hongik.devtalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevtalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevtalkApplication.class, args);
	}

}
