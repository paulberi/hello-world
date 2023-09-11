package fileWriterAndReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ReadFile {
	
	public static void overWrite(String tempFile, String originalFile) {
		
		
		try {
			FileWriter fw=new FileWriter(originalFile);
			FileReader fr=new FileReader(tempFile);
			
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
		File temFile=new File(tempFile);
		temFile.deleteOnExit();
	}

}
