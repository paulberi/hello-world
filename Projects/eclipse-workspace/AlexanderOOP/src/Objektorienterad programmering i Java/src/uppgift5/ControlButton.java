package uppgift5;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class ControlButton extends Button
{
	private final int width =  (int)(NumpadButton.SIZE *1.6);
	private final int heigth = NumpadButton.SIZE;
	
	ControlButton(String text, String colorTint)
	{
		super(text);
		
		setMinWidth(width);
		setMinHeight(heigth);

		getStyleClass().add("control-button");
		
		Color color = Color.valueOf(colorTint);
		Color darker = color.deriveColor(1, 1, .9, 1);
		
		setStyle("-fx-base: #"+color.toString().replace("0x", "").substring(0, 6));
		pressedProperty().addListener((observable, oldValue, newValue) ->
		{
			if (newValue.equals(true))
			{
				setStyle("-fx-base: #"+darker.toString().replace("0x", "").substring(0, 6));
			}
			else
			{
				setStyle("-fx-base: #"+color.toString().replace("0x", "").substring(0, 6));
			}
		});
		
	}
	ControlButton()
	{
		this("", "transparent");
	}
}