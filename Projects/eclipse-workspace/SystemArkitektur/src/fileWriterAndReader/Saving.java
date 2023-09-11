package fileWriterAndReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Saving {
	static String saved;
	
	Saving(String saved){
		this.saved=saved;
	}
	
	public static void save() {
		Scanner input=new Scanner(System.in);
		System.out.println("what is your first name");
		String userInput=input.nextLine();
		System.out.println("what is your surname");
		String userInput1=input.nextLine();
		
		try {
			FileWriter fw=new FileWriter(saved);
			fw.write("Name:"+userInput+"\nSurname:"+userInput1);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
