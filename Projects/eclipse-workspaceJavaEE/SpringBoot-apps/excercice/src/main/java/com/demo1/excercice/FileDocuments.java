package com.demo1.excercice;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Component;


@Component
public class FileDocuments {
	
	 public FileDocuments() {}

	 public  FileWriter textFile(String path) throws IOException {
		 
		 String javaEE="what is going on here"
					+ "\nit is very important that you listen"
					+ "\nwhat we are doing these days is very important"
					+ "\nbesides that as a programmer you are expected to be attentive"
					+ "\nif you want to succeed as a programmer you have to work hard"
					+ "\nthose who are good in maths statistically find programming easier";
			;
		 FileWriter writer=new FileWriter(path);
		 writer.write(javaEE);
		 writer.close();
		return writer;
		 
	 }

}
