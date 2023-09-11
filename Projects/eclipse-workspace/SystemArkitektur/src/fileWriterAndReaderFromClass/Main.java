package fileWriterAndReaderFromClass;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FileHandler fileHandler =new FileHandler();
		Scanner reader=new Scanner(System.in);
		
		System.out.println("What key");
		
		String key=reader.next();
		
		System.out.println("Current " + key +": "+fileHandler.read(key));
		System.out.println("New "+key+ "?");
		
		String value=reader.next();
		
		fileHandler.write(key,value);
		
		System.out.println("Current "+key+": "+fileHandler.read(key));
		
		fileHandler.save();

	}

}
