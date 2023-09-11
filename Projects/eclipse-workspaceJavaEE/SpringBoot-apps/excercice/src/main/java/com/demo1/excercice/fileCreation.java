package com.demo1.excercice;

import java.io.FileWriter;
import java.io.IOException;

public class fileCreation {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 String javaEE="Hello world! \nWhat is the population of the world \nIt is impossible to tell";
		 FileWriter writer=new FileWriter("Users/paulberinyuylukong/Desktop/text.txt");
		 writer.write(javaEE);
		 writer.close();
	}

}
