package uppgift5;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class ReceiptRow extends HBox
{
	public ReceiptRow(String text, String value)
	{
		setSpacing(4);

		Label textLabel  = new Label(text);
		Label valueLabel = new Label(value);
		getChildren().addAll(textLabel, valueLabel);
	}
}
