
package uppgift4;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class EditPopup extends Stage
{
	ObjectProperty<Person> person = new SimpleObjectProperty<Person>();
	BooleanProperty        isOpen = new SimpleBooleanProperty(true);

	public EditPopup(Person item)
	{
		setResizable(false);
		setTitle("Edit item");
		initModality(Modality.NONE);
		VBox column = new VBox(8);
		column.setStyle("-fx-padding: 16 16 16 16;");
		column.setAlignment(Pos.TOP_CENTER);

		PersonFields personFields = new PersonFields();
		personFields.setFirstName(item.getFirstName());
		personFields.setLastName(item.getLastName());
		personFields.setAge(String.valueOf(item.getAge()));

		Button saveButton = new Button("Save");
		saveButton.disableProperty().bind(Bindings.isEmpty(personFields.firstNameProperty).or(Bindings.isEmpty(personFields.lastNameProperty)).or(Bindings.isEmpty(personFields.ageProperty)));
		saveButton.setOnMouseClicked(e ->
		{
			if (e.getButton().equals(MouseButton.PRIMARY))
			{
				person.setValue(new Person(personFields.firstNameProperty.getValue(), personFields.lastNameProperty.getValue(), Integer.parseInt(personFields.ageProperty.getValue())));
			}

			close();
		});

		column.getChildren().addAll(personFields, saveButton);
		Scene scene = new Scene(column, 200, 220);
		scene.getStylesheets().add(getClass().getResource("Theme.css").toExternalForm());
		scene.setOnMousePressed(event ->
		{
			column.requestFocus();
		});
		setScene(scene);
		show();

		scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event ->
		{
			isOpen.setValue(false);
		});
	}
}
