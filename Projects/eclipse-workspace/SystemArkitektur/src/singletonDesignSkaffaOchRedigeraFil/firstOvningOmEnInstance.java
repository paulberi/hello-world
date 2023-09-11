package singletonDesignSkaffaOchRedigeraFil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//frist singleton but still dont know what singleton is
public class firstOvningOmEnInstance {
	private static HashMap<String, String> conf1=new HashMap<>();
	private List<String>id=new ArrayList<String>();
	private static String filePath="/Users/paulberinyuylukong/Desktop/file.cfg";
	
	private static firstOvningOmEnInstance instance;
	
	firstOvningOmEnInstance() throws IOException {
		List<String>lines=Files.readAllLines(Paths.get(filePath));
		for (String line:lines) {
			String[] parts=line.split( ":");
			conf1.put(parts[0], parts[1]);
			
			
			
		}
	}
	private static void saveConfiguration() {
		String configData="";
		for(String key:conf1.keySet()) {
			configData+=key+":"+conf1.get(key)+"\n";
		}
		
		try {
			FileWriter fw=new FileWriter(new File(filePath),false);
			fw.write(configData);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void save(String key, String value) {
		conf1.put(key, value);
		saveConfiguration();
	}
	public static String read(String key) {
		save("Beri", "Paul");
		if(conf1.keySet().contains(key)) {
			return conf1.get(key);
		}
		
		return "key not found";
	}
	public static   firstOvningOmEnInstance getInstance() throws IOException {
		if(instance==null) {
			instance= new firstOvningOmEnInstance();
		}
		return instance;
		
		
	}
}
