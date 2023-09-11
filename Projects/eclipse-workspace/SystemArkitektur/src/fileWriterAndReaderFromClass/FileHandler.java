package fileWriterAndReaderFromClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {
	
	private ArrayList<String> data=new ArrayList<String>();
	private File myObj= new File("/Users/paulberinyuylukong/Desktop/file.cfg");
	
	public FileHandler() {
		try {
			Scanner myReader=new Scanner(myObj);
			while(myReader.hasNextLine()) {
				data.add(myReader.nextLine());
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String read(String key) {
		
		for(String line:data) {
			String[] temp=line.split(":");
			if(temp[0].equals(key)) {
				return temp[1];
			}
		}
		return "key not found";
	}

	public void write(String key, String value) {
		boolean keyFound=false;
		for(int i=0; i<data.size(); i++) {
			String[] temp = data.get(i).split(":");
			if(temp[0].equals(key)) {
				data.remove(i);
				data.add(key+": "+value);
				keyFound=true;
			}
		}
		if(!keyFound) {
			data.add(key+": "+value);
		}
		
	}

	public void save() {
		try {
			FileWriter myWriter=new FileWriter(myObj);
			for(String line:data) {
				myWriter.write(line);
				if(!line.equals(data.get(data.size()-1))) {
					myWriter.write("\n");
				}
			}
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
