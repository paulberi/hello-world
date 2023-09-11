package com.yajava.v39demo2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;

import java.util.Scanner;

@SpringBootApplication
public class V39demo2Application implements CommandLineRunner {

	@Autowired
	private StringProcessor stringProcessor;

	@Autowired
	private DemoService demoService;

	@Bean
	StringProcessor stringService() {
		return new StringProcessor();
	}

	public static void main(String[] args) {
		SpringApplication.run(V39demo2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Skriv in din text:");
		Scanner scanner = new Scanner(System.in);
		String text = scanner.nextLine();
		System.out.println("Reverse string is: " + stringProcessor.reverse(text));
		System.out.println("Length of string is: " + stringProcessor.stringLength(text));
		System.out.println("Prefixed: " + stringService().prefixString(text));
		System.out.println("Reversed by bean: " + demoService.textReverse(text));
	}
}
