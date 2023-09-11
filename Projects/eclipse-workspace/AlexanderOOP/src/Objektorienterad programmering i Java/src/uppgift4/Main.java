package uppgift4;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application
{
	public static final int    MIN_WIDTH  = 300;
	public static final int    MIN_HEIGHT = 480;
	public static final String XML_PATH   = "persons.xml";

	private PersonTable personTable = new PersonTable();
	private XMLHandler  xmlHandler  = new XMLHandler();

	public static void main(String[] args)
	{
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		VBox mainColumn = new VBox(16);
		mainColumn.setAlignment(Pos.TOP_LEFT);
		mainColumn.setStyle("-fx-padding: 16 16 16 16;");

		VBox fieldsAndButtonsColumn = new VBox(16);
		fieldsAndButtonsColumn.setMaxWidth(TextInput.MAX_WIDTH);
		
		// Input
		PersonFields personFields = new PersonFields();
		fieldsAndButtonsColumn.getChildren().add(personFields);

		// Buttons
		HBox buttonsRow = new HBox();
		buttonsRow.setSpacing(8);
		buttonsRow.setAlignment(Pos.BASELINE_RIGHT);
		Button addButton = new Button("Add");
		addButton.disableProperty().bind(Bindings.isEmpty(personFields.firstNameProperty).or(Bindings.isEmpty(personFields.lastNameProperty)).or(Bindings.isEmpty(personFields.ageProperty)));
		Button reloadButton = new Button("Reload");
		Button clearButton   = new Button("Clear");
		buttonsRow.getChildren().addAll(addButton, reloadButton, clearButton);
		fieldsAndButtonsColumn.getChildren().add(buttonsRow);
		mainColumn.getChildren().add(fieldsAndButtonsColumn);

		// Table
		personTable.prefHeightProperty().bind(primaryStage.heightProperty());
		personTable.prefWidthProperty().bind(primaryStage.widthProperty());
		mainColumn.getChildren().add(personTable);
		clearButton.disableProperty().bind(Bindings.isEmpty(personTable.getItems()));
		personTable.getItems().addAll(xmlHandler.load());

		// Connect buttons
		addButton.setOnMouseClicked(e ->
		{
			if (e.getButton().equals(MouseButton.PRIMARY))
			{
				personTable.getItems().add(new Person(personFields.firstNameProperty.getValue(), personFields.lastNameProperty.getValue(), Integer.parseInt(personFields.ageProperty.getValue())));
				personFields.setFirstName("");
				personFields.setLastName("");
				personFields.setAge("");
			}

		});
		reloadButton.setOnMouseClicked(e ->
		{
			if (e.getButton().equals(MouseButton.PRIMARY))
			{
				personTable.getItems().setAll(xmlHandler.load());
			}

		});
		clearButton.setOnMouseClicked(e ->
		{
			if (e.getButton().equals(MouseButton.PRIMARY))
			{
				personTable.getItems().clear();
			}

		});

		// Overlay
		Overlay overlay = new Overlay();
		overlay.addEventHandler(overlay.closePopup, event ->
		{
			personTable.closePopup();
		});
		personTable.popupOpen.addListener((observable, oldValue, newValue) ->
		{
			if (newValue)
				overlay.fadeOut();
			else
				overlay.fadeIn();
		});

		/* Backdrop */
		Backdrop backdropView = new Backdrop();
		StackPane.setAlignment(backdropView, Pos.BOTTOM_RIGHT); 
		
		// Scene and stage
		StackPane stackPane = new StackPane();
		stackPane.getChildren().addAll(backdropView, mainColumn, overlay);
		Scene scene = new Scene(stackPane, MIN_WIDTH, MIN_HEIGHT);
		scene.setOnMousePressed(event ->
		{
			mainColumn.requestFocus();
		});
		overlay.widthProperty().bind(scene.widthProperty());
		overlay.heightProperty().bind(scene.heightProperty());
		primaryStage.setMinWidth(MIN_WIDTH);
		primaryStage.setMinHeight(MIN_HEIGHT);
		primaryStage.setTitle("Uppgift 4: Personhanteraren");
		primaryStage.setScene(scene);
		scene.getStylesheets().add(this.getClass().getResource("Theme.css").toExternalForm());
		mainColumn.requestFocus();
		primaryStage.show();

		personTable.setMaxHeight(primaryStage.getMaxHeight());
	}


	@Override
	public void stop()
	{
		xmlHandler.save(personTable.getItems().toArray());
	}
}
