package com.demo1.excercice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileWriterService {

	@Autowired
	private  ApplicationContext applicationContext;
	
	
	static FileDocuments files;
	static FileWriter writer;
	public void test() {
		System.out.println("all right to here");
	}

	public  FileWriter lineCounter(String path, String path1) throws IOException, ClassNotFoundException {

		files=(FileDocuments) applicationContext.getBean("fileService");
		writer = files.textFile(path);
		FileWriter writer1=new FileWriter(path1);
		FileReader fr = new FileReader(path);
		BufferedReader br=new BufferedReader(fr);
		System.out.println("hello");
		try {
			int count = 1;
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line=br.readLine();
				writer1.write(count+" "+line+"\n");

				sb.append(count++);
				System.out.println(line);
			}
			br.close();
			writer1.close();
		} finally {
			
		}
		return writer1;
	}
	
	
	public  void readingFile(String path1) throws IOException {
		

		List<String> list = new ArrayList<>();

		Scanner s=new Scanner(new File(path1));
		while(s.hasNextLine()) { //use hasNext and Next if you want individual words or char array if you want individual chars
			list.add(s.nextLine());
		}
		s.close();
		for(String st:list) {
			System.out.println(st);
		}
	}
}
