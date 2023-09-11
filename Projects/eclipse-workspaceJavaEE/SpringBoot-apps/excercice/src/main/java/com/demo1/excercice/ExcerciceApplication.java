package com.demo1.excercice;

import java.io.File;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExcerciceApplication implements CommandLineRunner{

	@Autowired
	private FileDocuments fileDocuments;
	
	@Autowired 
	private FileWriterService service;
	
	@Bean
	FileDocuments fileService() {
		return new FileDocuments();
	}
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(ExcerciceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("hello");
		
		Scanner scanner = new Scanner(System.in);
		
		
		System.out.println("path");
		String path = scanner.nextLine();
		
		System.out.println("path1");
		String path1 = scanner.nextLine();

		service.test();
		service.lineCounter(path, path1);
		//String path = "/Users/paulberinyuylukong/Desktop/text.txt";
		//static String path1 = "/Users/paulberinyuylukong/Desktop/text.txt";
		//static String path = "/Users/paulberinyuylukong/Desktop/paul.txt";
		//BufferedReader br=new BufferedReader(new FileReader(path));
		System.out.println("saving now to list");
		service.readingFile(path1);

		
		
		
	}

}
