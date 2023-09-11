package singletonDesignSkaffaOchRedigeraFil;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class Configuration {
	
	private String filePath="/Users/paulberinyuylukong/Desktop/file.cfg";
	private HashMap<String, String> conf1=new HashMap<>();
	
	
	public Configuration() throws IOException {
		List<String>lines=Files.readAllLines(Paths.get(filePath));
		for (String line:lines) {
			String[] parts=line.split( ":");
			conf1.put(parts[0], parts[1]);
			
			
			
		}
	}
	private void saveConfiguration() {
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
	public void save(String key, String value) {
		conf1.put(key, value);
		saveConfiguration();
	}
	public String read(String key) {
		if(conf1.keySet().contains(key)) {
			return conf1.get(key);
		}
		return "key not found";
	}

}
