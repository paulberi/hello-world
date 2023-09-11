package com.tools;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class WritingAndEditingFiles {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String origin ="/Users/paulberinyuylukong/Desktop/paul1.txt";
		String temp ="/Users/paulberinyuylukong/Desktop/temp.txt";
		boolean rowEdited=false;
		int rowNumber;
		int rowCounter = 0;
		String newLine;
		//Scanner in=new Scanner(System.in);
		int i;
		try {
			Scanner in=new Scanner(System.in);
			FileWriter fw= new FileWriter(temp);
			FileReader fr=new FileReader(origin);
			System.out.println("write down the row number you want to edit");
			rowNumber=in.nextInt();
			
			if(rowNumber==1) {
				System.out.println("write the change");
				newLine=in.next();//nextLine not working check why later
			}else {
				System.out.println("write the replacement");
				newLine='\n'+in.next();
			}
			while((i=fr.read())!=-1) {
				if(i=='\n') {
					rowCounter++;
				}
				if(rowCounter==rowNumber) {
					while(!rowEdited) {
						fw.write(newLine);
						rowEdited=true;
					}
					continue;
				}
				else {
					fw.write(((char)i));
				}
				
			}
			fr.close();
			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disposingTempFile(temp,origin);
		
	}

	private static void disposingTempFile(String temp,String origin) {
		// TODO Auto-generated method stub
		
		origin ="/Users/paulberinyuylukong/Desktop/paul1.txt";
		temp ="/Users/paulberinyuylukong/Desktop/temp.txt";
		
		try {
			FileWriter fw=new FileWriter(origin);
			FileReader fr=new FileReader(temp);
			int i;
			while((i=fr.read())!=-1) {
				fw.write(((char)i));
			}
			fr.close();
			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File temp1=new File(temp);
		temp1.deleteOnExit();
		
	}

}
