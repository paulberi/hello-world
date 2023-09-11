package uppgift4;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.VBox;


public class PersonFields extends VBox
{
	StringProperty firstNameProperty = new SimpleStringProperty();
	StringProperty lastNameProperty  = new SimpleStringProperty();
	StringProperty ageProperty       = new SimpleStringProperty();

	private TextInput firstNameInput;
	private TextInput lastNameInput;
	private TextInput ageInput;

	public PersonFields()
	{
		setSpacing(8);

		firstNameInput = new TextInput("First name");
		firstNameProperty.bind(firstNameInput.textProperty);
		lastNameInput = new TextInput("Last name");
		lastNameProperty.bind(lastNameInput.textProperty);
		ageInput = new TextInput("Age");
		ageProperty.bind(ageInput.textProperty);
		ageInput.setDigitsOnly(true);

		getChildren().addAll(firstNameInput, lastNameInput, ageInput);
	}

	public void setFirstName(String value)
	{
		firstNameInput.setText(value);
	}

	public void setLastName(String value)
	{
		lastNameInput.setText(value);
	}

	public void setAge(String value)
	{
		ageInput.setText(value);
	}
}
