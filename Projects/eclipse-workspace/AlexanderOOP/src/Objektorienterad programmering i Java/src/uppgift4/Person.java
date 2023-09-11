package uppgift4;

import java.io.Serializable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Person implements Serializable
{
	private static final long serialVersionUID = 7356239926166115193L;

	private StringProperty  firstName = new SimpleStringProperty();
	private StringProperty  lastName  = new SimpleStringProperty();;
	private IntegerProperty age       = new SimpleIntegerProperty();
	private IntegerProperty id        = new SimpleIntegerProperty();

	public Person()
	{
	}

	public Person(String firstName, String lastName, int age)
	{
		setFirstName(firstName);
		this.lastName.set(lastName);
		this.age.set(age);
	}

	public String getFirstName()
	{
		return firstName.get();

	}

	public void setFirstName(String value)
	{
		firstName.set(value);

	}

	public String getLastName()
	{
		return lastName.get();

	}

	public void setLastName(String value)
	{
		lastName.set(value);

	}

	public int getAge()
	{
		return age.get();

	}

	public void setAge(int value)
	{
		age.set(value);
	}
}
