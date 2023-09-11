package singletonDesignSkaffaOchRedigeraFil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class arrayListVersion {
	private String name;
	private String Paul;
	
	private List<Object>id=new ArrayList<Object>();
	private String filePath="/Users/paulberinyuylukong/Desktop/file.cfg";
	
	
	
	
	arrayListVersion() throws IOException {
		List<String> person=Files.readAllLines(Paths.get(filePath));
			id.add(person);
		}
	
	void saveConfiguration() {
		String configData="Name "+"Paul";
	
		try {
			FileWriter fw=new FileWriter(new File(filePath),false);
			fw.write(configData);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String read(String key) {
		saveConfiguration();
		System.out.println(id);
		for(Object x:id) {
			if(((List<Object>) x).contains(key)) {
				System.out.println(id.get(0));
				System.out.println(x);
			}else {
				System.out.println("key not found");
			}
		}
		return "key not found";
		
	}
	

}
