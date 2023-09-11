package com.yajava.v39demo1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class V39demo1Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(V39demo1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Demo 1 - Skapa Spring Boot projekt");
	}
}
