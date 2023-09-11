package uppgift5;

import javafx.scene.control.Label;

public class Header extends Label
{
	public Header(String text)
	{
		setText(text.toUpperCase());
		setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white;");
	}
}
