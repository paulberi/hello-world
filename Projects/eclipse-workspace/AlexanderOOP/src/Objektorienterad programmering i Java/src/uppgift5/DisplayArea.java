package uppgift5;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class DisplayArea extends HBox
{
	public static final int BUTTONS_SPACING = 32;

	private Display         display;
	private StringProperty  labelTextProperty     = new SimpleStringProperty();
	private StringProperty  subTextProperty       = new SimpleStringProperty();
	private StringProperty  inputTextProperty     = new SimpleStringProperty();
	private StringProperty  inputFeedbackProperty = new SimpleStringProperty();
	private int             sideButtonClickedIndex;
	public EventType<Event> sideButtonClicked     = new EventType<Event>("SIDEBUTTON_CLICKED");

	public DisplayArea()
	{
		setSpacing(8);
		setAlignment(Pos.CENTER);

		VBox leftButtons = new VBox(BUTTONS_SPACING);
		leftButtons.setAlignment(Pos.CENTER);

		for (int i = 0; i < 4; i++)
		{
			NumpadButton b = new NumpadButton("-->");
			int n = i;
			b.setOnMouseClicked(e ->
			{
				sideButtonClickedIndex = n;
				fireEvent(new Event(sideButtonClicked));
			});
			leftButtons.getChildren().add(b);
		}

		display = new Display();
		display.labelTextProperty.bind(labelTextProperty);
		display.subTextProperty.bind(subTextProperty);
		display.inputTextProperty.bind(inputTextProperty);
		display.inputFeedbackProperty.bind(inputFeedbackProperty);

		VBox rightButtons = new VBox(BUTTONS_SPACING);
		rightButtons.setAlignment(Pos.CENTER);
		for (int i = 4; i < 8; i++)
		{
			NumpadButton b = new NumpadButton("<--");
			int n = i;
			b.setOnMouseClicked(e ->
			{
				sideButtonClickedIndex = n;
				fireEvent(new Event(sideButtonClicked));
			});
			rightButtons.getChildren().add(b);
		}

		getChildren().addAll(leftButtons, display, rightButtons);
	}

	public void setSideButtonTexts(String left1, String left2, String left3, String left4, String right1, String right2, String right3, String right4)
	{
		display.setLeft1TextProperty(left1);
		display.setLeft2TextProperty(left2);
		display.setLeft3TextProperty(left3);
		display.setLeft4TextProperty(left4);
		display.setRight1TextProperty(right1);
		display.setRight2TextProperty(right2);
		display.setRight3TextProperty(right3);
		display.setRight4TextProperty(right4);
	}

	public void clearSideButtonTexts()
	{
		display.setLeft1TextProperty("");
		display.setLeft2TextProperty("");
		display.setLeft3TextProperty("");
		display.setLeft4TextProperty("");
		display.setRight1TextProperty("");
		display.setRight2TextProperty("");
		display.setRight3TextProperty("");
		display.setRight4TextProperty("");
	}

	public void setLabelTextProperty(String str)
	{
		labelTextProperty.setValue(str);
	}

	public void setSubTextProperty(String str)
	{
		subTextProperty.setValue(str);
	}

	public void setInputTextProperty(String str)
	{
		inputTextProperty.setValue(str);
	}

	public int getSideButtonClickedIndex()
	{
		return sideButtonClickedIndex;
	}

	public void showWrongPinCode()
	{
		display.showWrongPinCode();
	}

	public void showBalanceTooLow()
	{
		display.showBalanceTooLow();
	}

	public void showText(String text)
	{
		display.showText(text);
	}

	public void setInputFeedbackProperty(String feedback)
	{
		inputFeedbackProperty.setValue(feedback);
	}
}
