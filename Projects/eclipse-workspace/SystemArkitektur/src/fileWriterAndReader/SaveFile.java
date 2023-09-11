package fileWriterAndReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SaveFile {
	
	static Scanner input=new Scanner(System.in);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String saved="/Users/paulberinyuylukong/Desktop/file.cfg";
		String tempFile="/Users/paulberinyuylukong/Desktop/file.txt";
		
		Saving saving=new Saving(saved);
		saving.save();
		System.out.println("which line do you want to edit");
		String changes=input.nextLine();
		int rowNumber=Integer.parseInt(changes);
		
		try {
			FileWriter fw=new FileWriter(tempFile);
			FileReader fr=new FileReader(saved);
			String newLine;
			int i;
			int rowCount = 0;
			boolean editedRow=false;
			if (rowNumber==1) {
				newLine = input.nextLine();
			}else {
				newLine='\n'+input.nextLine();
			}
			while((i=fr.read())!=-1) {
				if(i=='\n') {
					rowCount++;
				}if(rowCount==rowNumber) {
					while(!editedRow) {
						fw.write(newLine);
						editedRow=true;
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
		
		ReadFile.overWrite(tempFile, saved);

	}

}
