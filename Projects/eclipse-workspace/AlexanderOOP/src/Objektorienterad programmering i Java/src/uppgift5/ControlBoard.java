package uppgift5;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class ControlBoard extends HBox
{
	EventType<Event> numpedClicked = new EventType<Event>("NUMPAD_CLICKED");
	EventType<Event> cancelClicked = new EventType<Event>("CANCEL_CLICKED");
	EventType<Event> clearClicked  = new EventType<Event>("CLEAR_CLICKED");
	EventType<Event> enterClicked  = new EventType<Event>("ENTER_CLICKED");
	private int numpadNumer;
	
	public ControlBoard()
	{
		setAlignment(Pos.CENTER);
		setSpacing(16);

		GridPane numpad = new GridPane();
		getChildren().add(numpad);
		numpad.setHgap(4);
		numpad.setVgap(4);
		for (int y = 0; y < 3; y++)
		{
			for (int x = 0; x < 3; x++)
			{
				NumpadButton b = new NumpadButton(String.valueOf((x + (y * 3)) + 1));
				b.setOnMouseClicked((MouseEvent m) -> {
					setNumpadNumer(Integer.parseInt(b.getText()));
					fireEvent(new Event(numpedClicked));
				});
				numpad.add(b , x, y);
			}

		}
		numpad.add(new NumpadButton(""), 0, 3);
		NumpadButton zeroButton = new NumpadButton("0");
		zeroButton.setOnMouseClicked((MouseEvent m) -> {
			setNumpadNumer(0);
			fireEvent(new Event(numpedClicked));
		});
		numpad.add(zeroButton, 1, 3);
		numpad.add(new NumpadButton(""), 2, 3);

		VBox controls = new VBox(4);
		ControlButton cancelButton = new ControlButton("Avbryt", "red");
		cancelButton.setOnMouseClicked(e -> {fireEvent(new Event(cancelClicked));});
		ControlButton clearButton  = new ControlButton("Rensa", "yellow");
		clearButton.setOnMouseClicked(e -> {fireEvent(new Event(clearClicked));});
		ControlButton emptyButton  = new ControlButton();
		emptyButton.setVisible(false);
		ControlButton enterButton  = new ControlButton("Klar", "green");
		enterButton.setOnMouseClicked(e -> {fireEvent(new Event(enterClicked));});
		
		controls.getChildren().addAll(cancelButton, clearButton, emptyButton, enterButton);
		getChildren().add(controls);
	}

	public int getNumpadNumer()
	{
		return numpadNumer;

	}

	private void setNumpadNumer(int numpadNumer)
	{
		this.numpadNumer = numpadNumer;

	}
}
