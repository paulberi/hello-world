package com.tools;

import java.io.FileReader;
import java.io.FileWriter;

public class FileWriter_Reader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			FileWriter fw=new FileWriter("/Users/paulberinyuylukong/Desktop/paul1");
			fw.write("what is going on here"
					+ "\nit is very important that you listen"
					+ "\nwhat we are doing these days is very important"
					+ "\nbesides that as a programmer you are expected to be attentive"
					+ "\nif you want to succeed as a programmer you have to work hard"
					+ "\nthose who are good in maths statistically find programming easier");
			fw.close();
			
			
		}catch(Exception e) {
			System.out.println(e);
		}
		System.out.println("success...!");
		
		try {
			FileReader fr= new FileReader("/Users/paulberinyuylukong/Desktop/paul1");
			int i;
			while((i=fr.read())!=-1) {
				System.out.print((char)i);
			}
			fr.close();
			
		}catch(Exception e) {
			System.out.println(e);
		}

	}
}
