package XMLtest;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Main {
	private static final String SERIALIZED_FILE_NAME="Student.xml";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Student student=new Student();
		student.setCourses("biology");
		student.setName("hello");
		
		try {
			XMLEncoder encoder=new XMLEncoder(new BufferedOutputStream(new FileOutputStream(SERIALIZED_FILE_NAME)));
			//FileOutputStream fos=new FileOutputStream(new File("./student.xml"));
			//XMLEncoder encoder= new XMLEncoder(fos);
			encoder.writeObject(student);
			encoder.flush();
			encoder.close();
			//fos.close();
			System.out.println("successful");
			
		}catch(IOException ex) {
			ex.printStackTrace();
			
		}
		
		try {
			XMLDecoder decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(SERIALIZED_FILE_NAME)));
			
			//FileInputStream fis=new FileInputStream(new File("./student.xml"));
			//XMLDecoder decoder= new XMLDecoder(fis);
			
			Student student1=(Student)decoder.readObject();
			decoder.close();
			//fis.close();
			
			System.out.println(student1.name);
			
			
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}

	}

}
