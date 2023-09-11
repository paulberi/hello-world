package uppgift5;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class DepositPopup extends Stage
{
	BooleanProperty isOpen = new SimpleBooleanProperty(true);
	IntegerProperty sum    = new SimpleIntegerProperty();

	private IntegerProperty notes20   = new SimpleIntegerProperty();
	private IntegerProperty notes50   = new SimpleIntegerProperty();
	private IntegerProperty notes100  = new SimpleIntegerProperty();
	private IntegerProperty notes200  = new SimpleIntegerProperty();
	private IntegerProperty notes500  = new SimpleIntegerProperty();
	private IntegerProperty notes1000 = new SimpleIntegerProperty();

	public DepositPopup()
	{
		setResizable(false);
		initModality(Modality.APPLICATION_MODAL);

		VBox mainColumn = new VBox(8);
		mainColumn.setPadding(new Insets(16));
		mainColumn.setAlignment(Pos.CENTER);

		Label header = new Label("Välj sedlar att sätta in");
		mainColumn.getChildren().add(header);

		GridPane inputGrid = new GridPane();
		mainColumn.getChildren().add(inputGrid);

		Label            notes20Label   = new Label("20-lappar:");
		Spinner<Integer> notes20Spinnes = new Spinner<Integer>(0, 100, 0);
		notes20.bind(notes20Spinnes.valueProperty());
		inputGrid.add(notes20Label, 0, 0);
		inputGrid.add(notes20Spinnes, 1, 0);

		Label            notes50Label   = new Label("50-lappar:");
		Spinner<Integer> notes50Spinner = new Spinner<Integer>(0, 100, 0);
		notes50.bind(notes50Spinner.valueProperty());
		inputGrid.add(notes50Label, 0, 1);
		inputGrid.add(notes50Spinner, 1, 1);
		
		Label            notes100Label   = new Label("100-lappar:");
		Spinner<Integer> notes100Spinner = new Spinner<Integer>(0, 100, 0);
		notes100.bind(notes100Spinner.valueProperty());
		inputGrid.add(notes100Label, 0, 2);
		inputGrid.add(notes100Spinner, 1, 2);
		
		Label            notes200Label   = new Label("200-lappar:");
		Spinner<Integer> notes200Spinner = new Spinner<Integer>(0, 100, 0);
		notes200.bind(notes200Spinner.valueProperty());
		inputGrid.add(notes200Label, 0, 3);
		inputGrid.add(notes200Spinner, 1, 3);
		
		Label            notes500Label   = new Label("500-lappar:");
		Spinner<Integer> notes500Spinner = new Spinner<Integer>(0, 100, 0);
		notes500.bind(notes500Spinner.valueProperty());
		inputGrid.add(notes500Label, 0, 4);
		inputGrid.add(notes500Spinner, 1, 4);
		
		Label            notes1000Label   = new Label("1000-lappar:");
		Spinner<Integer> notes1000Spinner = new Spinner<Integer>(0, 100, 0);
		notes1000.bind(notes1000Spinner.valueProperty());
		inputGrid.add(notes1000Label, 0, 5);
		inputGrid.add(notes1000Spinner, 1, 5);
		
		Label sumLabel = new Label("0 kr");
		StringProperty sumPromptProperty = new SimpleStringProperty("Summa: ");
		sum.bind(notes20.multiply(20).add(notes50.multiply(50)).add(notes100.multiply(100)).add(notes200.multiply(200)).add(notes500.multiply(500)).add(notes1000.multiply(1000)));
		sumLabel.textProperty().bind(sumPromptProperty.concat(sum.asString()).concat(" kr"));
		sumLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
		mainColumn.getChildren().add(sumLabel);
		
		Button confirmButton = new Button("Bekräfta");
		confirmButton.setOnMouseClicked(e -> {isOpen.setValue(false); close();});
		mainColumn.getChildren().add(confirmButton);

		Scene scene = new Scene(mainColumn);
		setTitle("Ny insättning");
		setScene(scene);
		show();

		scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event ->
		{
			sum.unbind();
			sum.setValue(0);
			isOpen.setValue(false);
		});
	}
}
