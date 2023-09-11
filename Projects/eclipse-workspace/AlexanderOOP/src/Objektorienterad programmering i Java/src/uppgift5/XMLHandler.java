package uppgift5;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;


public class XMLHandler
{
	private static final String XML_PATH = "Accounts.xml";

	public static void save(Object[] objectList)
	{
		XMLEncoder xmlEncoder = null;
		try
		{
			xmlEncoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(XML_PATH)));
			xmlEncoder.writeObject(objectList);
			xmlEncoder.close();
		}
		catch (FileNotFoundException fileNotFound)
		{
			System.out.println("Error: Unable to save to xml file (" + XML_PATH + ").");
		}

	}

	public static ListProperty<Account> load()
	{
		ListProperty<Account> accounts = new SimpleListProperty<Account>(FXCollections.observableArrayList(new ArrayList<Account>()));
		;
		XMLDecoder xmlDecoder = null;
		try
		{
			xmlDecoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(XML_PATH)));
			Object[] objects = (Object[]) xmlDecoder.readObject();
			for (Object object : objects)
				accounts.add((Account) object);
		}
		catch (FileNotFoundException fileNotFound)
		{
			System.out.println("Error: Unable to read xml file (" + XML_PATH + ").");
		}

		return accounts;
	}
}
