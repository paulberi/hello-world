package uppgift4;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class TextInput extends VBox
{
	public static final int MAX_WIDTH = 600;
	
	private Label     label      = new Label();
	private TextField textField  = new TextField();
	private Boolean   digitsOnly = false;

	protected StringProperty textProperty = textField.textProperty();

	public TextInput(String promptText)
	{
		textField.getStyleClass().add("text-input");
		textField.setMaxWidth(MAX_WIDTH);
		textField.textProperty().addListener((observable, oldValue, newValue) ->
		{
			if (newValue.length() == 0)
				this.getChildren().remove(label);
			else if (this.getChildren().size() == 1)
				this.getChildren().add(label);

			if (digitsOnly && !newValue.matches("^[0-9]*"))
				textField.setText(oldValue);
		});
		textField.focusedProperty().addListener((observable) ->
		{
			if (!textField.isFocused() && textField.getText().length() == 0)
				this.getChildren().remove(label);
			else if (this.getChildren().size() == 1 && textField.getText().length() > 0)
				this.getChildren().add(label);
		});

		setPromptText(promptText);
		this.getChildren().addAll(textField);
	}

	public void setText(String value)
	{
		textField.setText(value);
	}

	public String getText()
	{
		return textField.getText();
	}

	public void setPromptText(String value)
	{
		label.setText(value);
		textField.setPromptText(value);
	}

	public String getPromptText()
	{
		return textField.getPromptText();
	}

	public void setDigitsOnly(Boolean value)
	{
		digitsOnly = value;
	}
}
