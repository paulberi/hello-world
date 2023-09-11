package uppgift5;

import javafx.scene.control.Button;

public class NumpadButton extends Button
{
	protected static final int SIZE = 40;
	
	NumpadButton(String text)
	{
		super(text);
		
		setMinWidth(SIZE);
		setMinHeight(SIZE);

		getStyleClass().add("side-button");
	}
	NumpadButton()
	{
		this("");
	}
}
