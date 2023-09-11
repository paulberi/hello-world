package uppgift4;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class XMLHandler
{
	public void save(Object[] objectList)
	{
		XMLEncoder xmlEncoder = null;
		try
		{
			xmlEncoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(Main.XML_PATH)));
			xmlEncoder.writeObject(objectList);
			xmlEncoder.close();
		}
		catch (FileNotFoundException fileNotFound)
		{
			System.out.println("Error: Unable to save to xml file (" + Main.XML_PATH + ").");
		}

	}

	public ArrayList<Person> load()
	{
		ArrayList<Person> persons = new ArrayList<Person>();
		XMLDecoder xmlDecoder = null;
		try
		{
			xmlDecoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(Main.XML_PATH)));
			Object[] objects=  (Object[]) xmlDecoder.readObject();
			for (Object object : objects)
				persons.add((Person)object);
		}
		catch (FileNotFoundException fileNotFound)
		{
			System.out.println("Error: Unable to read xml file (" + Main.XML_PATH + ").");
		}
		
		return persons;
	}
}
